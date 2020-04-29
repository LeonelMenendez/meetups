import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { MeetupService } from 'src/app/core/services/meetup.service';
import { IInvitationsRequest } from 'src/app/shared/models/invitation';
import { IUser } from 'src/app/shared/models/user';

@Component({
  selector: 'app-invite-dialog',
  templateUrl: './invite-dialog.component.html',
  styleUrls: ['./invite-dialog.component.scss'],
})
export class InviteDialogComponent implements OnInit {
  form: FormGroup;
  meetupDescription: string;
  meetupId: number;
  users: IUser[];

  constructor(
    private formBuilder: FormBuilder,
    private meetupService: MeetupService,
    private toastr: ToastrService,
    private inviteDialogRef: MatDialogRef<InviteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) inviteData
  ) {
    this.meetupDescription = inviteData.meetupDescription;
    this.meetupId = inviteData.meetupId;
    this.users = inviteData.users;
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      guests: ['', Validators.required],
    });
  }

  get guests(): AbstractControl {
    return this.form.controls.guests;
  }

  get guestsError(): string {
    if (this.guests.hasError('required')) {
      return 'At least one invitation is required';
    }
  }

  private get invitationsRequest(): IInvitationsRequest {
    return this.guests.value;
  }

  send() {
    if (!this.form.valid) {
      return;
    }

    this.meetupService.createInvitations(this.meetupId, this.invitationsRequest).subscribe(() => {
      this.toastr.success('Invitations sent successfully');
      this.close();
    });
  }

  close() {
    this.inviteDialogRef.close();
  }
}
