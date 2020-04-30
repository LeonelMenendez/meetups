import { Model } from '../model';

export interface IMeetupResponse extends Model {
  day: Date;
  ownerId: number;
  ownerName: string;
  ownerEmail: string;
  temparature: number;
  beerCasesNeeded?: number;
}
