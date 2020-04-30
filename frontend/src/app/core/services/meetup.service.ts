import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IInvitationsRequest } from 'src/app/shared/models/invitation';
import { IMeetupRequest, IMeetupResponse } from 'src/app/shared/models/meetup';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MeetupService {
  private END_POINT_BASE = `${environment.API_URL}/meetups`;

  private END_POINT_INVITATIONS(meetupId: number): string {
    return `${this.END_POINT_BASE}/${meetupId}/invitations`;
  }

  constructor(private http: HttpClient) {}

  create(meetupRequest: IMeetupRequest): Observable<IMeetupResponse> {
    return this.http.post<IMeetupResponse>(this.END_POINT_BASE, meetupRequest);
  }

  createInvitations(meetupId: number, request: IInvitationsRequest): Observable<IMeetupResponse[]> {
    return this.http.post<IMeetupResponse[]>(this.END_POINT_INVITATIONS(meetupId), request);
  }
}
