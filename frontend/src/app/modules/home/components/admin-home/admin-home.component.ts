import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/services/auth.service';
import { MeetupService } from 'src/app/core/services/meetup.service';
import { UserService } from 'src/app/core/services/user.service';
import { WeatherService } from 'src/app/core/services/weather.service';
import { Role } from 'src/app/shared/enums/role';
import { IMeetupRequest, IMeetupResponse } from 'src/app/shared/models/meetup';
import { IUser } from 'src/app/shared/models/user';
import { IDayWeatherForecastResponse } from 'src/app/shared/models/weather';

import { MeetupsDataSource } from '../../../../shared/data-sources/meetups.data-source';
import { InviteDialogComponent } from '../invite-dialog/invite-dialog.component';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss'],
})
export class AdminHomeComponent implements OnInit {
  form: FormGroup;
  minDate: Date;
  maxDate: Date;
  dayWeatherForecastList: IDayWeatherForecastResponse[];

  users: IUser[];
  displayedColumns = ['day', 'temperature', 'beerCasesNeeded', 'actions'];
  meetupsDataSource: MeetupsDataSource;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private meetupService: MeetupService,
    private weatherService: WeatherService,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      day: ['', Validators.required],
      temperature: ['', Validators.required],
    });

    this.meetupsDataSource = new MeetupsDataSource();
    this.meetupsDataSource.load(this.userService);

    this.userService.findAll(Role.USER).subscribe((users: IUser[]) => {
      this.users = users;
    });
  }

  isDayWeatherForecastListLoaded() {
    return Array.isArray(this.dayWeatherForecastList) && this.dayWeatherForecastList.length;
  }

  loadDayWeatherForecastList() {
    if (!this.isDayWeatherForecastListLoaded()) {
      this.weatherService.getDailyWeatherForecast().subscribe((dayWeatherForecastList) => {
        this.dayWeatherForecastList = dayWeatherForecastList;
        this.minDate = moment(dayWeatherForecastList[0].day).toDate();
        this.maxDate = moment(
          dayWeatherForecastList[dayWeatherForecastList.length - 1].day
        ).toDate();
      });
    }
  }

  loadTemperature(selectedDay: Date) {
    if (this.isDayWeatherForecastListLoaded()) {
      const dayToCompare = moment(selectedDay);
      const dayWeatherForecast = this.dayWeatherForecastList.find((dayWeatherForecast) =>
        moment(dayWeatherForecast.day).isSame(moment(dayToCompare), 'day')
      );

      this.temperature.patchValue(dayWeatherForecast.temperature);
    }
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
      this.toastr.success('Meetup created successfully');
      this.meetupsDataSource.add(meetup);
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
