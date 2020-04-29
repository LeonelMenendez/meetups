export interface IMeetupResponse {
  id: number;
  day: Date;
  ownerId: number;
  ownerName: string;
  ownerEmail: string;
  tempareture: number;
  beerCasesNeeded?: number;
}
