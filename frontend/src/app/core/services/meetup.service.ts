import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IInvitationsRequest } from 'src/app/shared/models/invitation';
import { IMeetupRequest, IMeetupResponse } from 'src/app/shared/models/meetup';
import { environment } from 'src/environments/environment';

import { RequestService } from './request.service';

@Injectable({
  providedIn: 'root',
})
export class MeetupService {
  private END_POINT_BASE = `${environment.API_URL}/meetups`;
  private END_POINT_CREATED = `${this.END_POINT_BASE}/created`;
  private END_POINT_ENROLLED = `${this.END_POINT_BASE}/enrolled`;

  constructor(private http: HttpClient, private requestService: RequestService) {}

  create(meetupRequest: IMeetupRequest): Observable<IMeetupResponse> {
    return this.http.post<IMeetupResponse>(this.END_POINT_BASE, meetupRequest);
  }

  createInvitations(
    meetupId: number,
    invitationsRequest: IInvitationsRequest
  ): Observable<IMeetupResponse[]> {
    return this.http.post<IMeetupResponse[]>(
      `${this.END_POINT_BASE}/${meetupId}/invitations`,
      invitationsRequest
    );
  }

  createdMeetups(): Observable<IMeetupResponse> {
    const params = this.requestService.getHttpParams({ ownerId: this.requestService.userId });
    return this.http.get<IMeetupResponse>(this.END_POINT_CREATED, { params });
  }

  enrolledMeetups(): Observable<IMeetupResponse> {
    const params = this.requestService.getHttpParams({ userId: this.requestService.userId });
    return this.http.get<IMeetupResponse>(this.END_POINT_ENROLLED, { params });
  }
}
