import { Model } from '../model';

export interface IEnrollmentResponse extends Model {
  userId: number;
  meetupId: number;
  meetupOwnerName: string;
  meetupOwnerEmail: string;
  meetupDay: Date;
  meetupTemperature: number;
  checkedIn: boolean;
}
