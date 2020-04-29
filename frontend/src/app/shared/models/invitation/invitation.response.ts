import { InvitationStatus } from '../../enums/invitation-status';

export interface IInvitationResponse {
  meetupId: number;
  userId: number;
  status: InvitationStatus;
}
