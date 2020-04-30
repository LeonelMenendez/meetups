import { Component, OnInit } from '@angular/core';
import { InvitationService } from 'src/app/core/services/invitation.service';
import { InvitationStatus } from 'src/app/shared/enums/invitation-status';
import { IInvitationResponse } from 'src/app/shared/models/invitation';

import { InvitationsDataSource } from '../../../../shared/data-sources/invitations.data-source';

@Component({
  selector: 'app-invitations',
  templateUrl: './invitations.component.html',
  styleUrls: ['./invitations.component.scss'],
})
export class InvitationsComponent implements OnInit {
  InvitationStatus = InvitationStatus;
  invitations: IInvitationResponse[];
  displayedColumns = ['day', 'temperature', 'status'];
  invitationsDataSource: InvitationsDataSource;

  constructor(private invitationService: InvitationService) {}

  ngOnInit(): void {
    this.invitationsDataSource = new InvitationsDataSource();
    this.invitationsDataSource.load(this.invitationService);
  }

  acceptInvitation(element: IInvitationResponse) {
    this.changeInvitationStatus(element, InvitationStatus.ACCEPTED);
  }

  declineInvitation(element: IInvitationResponse) {
    this.changeInvitationStatus(element, InvitationStatus.DECLINED);
  }

  changeInvitationStatus(element: IInvitationResponse, status: InvitationStatus) {
    this.invitationService.changeStatus(element.id, status).subscribe(() => {
      element.status = status;
      this.invitationsDataSource.update(element);
    });
  }
}
