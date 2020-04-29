import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AuthService } from 'src/app/core/services/auth.service';
import { MeetupService } from 'src/app/core/services/meetup.service';
import { UserService } from 'src/app/core/services/user.service';
import { Role } from 'src/app/shared/enums/role';
import { IMeetupRequest, IMeetupResponse } from 'src/app/shared/models/meetup';
import { IUser } from 'src/app/shared/models/user';

import { MeetupsDataSource } from '../../data-sources/meetups.data-source';
import { InviteDialogComponent } from '../invite-dialog/invite-dialog.component';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss'],
})
export class AdminHomeComponent implements OnInit {
  form: FormGroup;
  users: IUser[];
  displayedColumns = ['day', 'temperature', 'beerCasesNeeded', 'actions'];
  meetupsDataSource: MeetupsDataSource;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private meetupService: MeetupService,
    private dialog: MatDialog,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      day: ['', Validators.required],
      temperature: ['', Validators.required],
    });

    this.meetupsDataSource = new MeetupsDataSource(this.meetupService);
    this.meetupsDataSource.loadCreatedMeetups();

    this.userService.findAll(Role.USER).subscribe((users: IUser[]) => {
      this.users = users;
    });
  }

  get day(): AbstractControl {
    return this.form.controls.day;
  }

  get temperature(): AbstractControl {
    return this.form.controls.temperature;
  }

  get dayError(): string {
    if (this.day.hasError('required')) {
      return 'The day is required';
    }
  }

  get temperatureError(): string {
    if (this.temperature.hasError('required')) {
      return 'The temperature is required';
    }
  }

  submit() {
    if (!this.form.valid) {
      return;
    }

    this.meetupService.create(this.meetupRequest).subscribe((meetup: IMeetupResponse) => {
      this.meetupsDataSource.addMeetup(meetup);
    });
  }

  private get meetupRequest(): IMeetupRequest {
    return {
      day: this.day.value,
      ownerId: this.authService.currentUserValue.id,
      temperature: this.temperature.value,
    };
  }

  openDialog(element: IMeetupResponse) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.restoreFocus = false;
    dialogConfig.data = {
      meetupDescription: `Meetup ${element.day}`,
      meetupId: element.id,
      users: this.users,
    };

    this.dialog.open(InviteDialogComponent, dialogConfig);
  }
}
