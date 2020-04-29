import { InvitationStatus } from '../../enums/invitation-status';

export interface IInvitationResponse {
  id: number;
  meetupId: number;
  userId: number;
  meetupOwnerName: string;
  meetupOwnerEmail: string;
  meetupDay: Date;
  meetupTemperature: number;
  status: InvitationStatus;
}
