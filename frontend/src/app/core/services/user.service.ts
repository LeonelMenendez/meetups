import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/shared/enums/role';
import { IEnrollmentResponse } from 'src/app/shared/models/enrollment';
import { IMeetupResponse } from 'src/app/shared/models/meetup';
import { IUser } from 'src/app/shared/models/user';
import { environment } from 'src/environments/environment';

import { RequestService } from './request.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private END_POINT_BASE = `${environment.API_URL}/users`;

  private END_POINT_ENROLLMENTS(): string {
    return `${this.END_POINT_BASE}/${this.requestService.userId}/enrollments`;
  }

  private END_POINT_MEETUPS_CREATED(): string {
    return `${this.END_POINT_BASE}/${this.requestService.userId}/meetups/created`;
  }

  private END_POINT_MEETUPS_ENROLLMENTS(): string {
    return `${this.END_POINT_BASE}/${this.requestService.userId}/meetups/enrolled`;
  }

  constructor(private http: HttpClient, private requestService: RequestService) {}

  findAll(role: Role): Observable<IUser[]> {
    const params = this.requestService.getHttpParams({ role });
    return this.http.get<IUser[]>(this.END_POINT_BASE, { params });
  }

  enrollments(): Observable<IEnrollmentResponse> {
    return this.http.get<IEnrollmentResponse>(this.END_POINT_ENROLLMENTS());
  }

  createdMeetups(): Observable<IMeetupResponse> {
    return this.http.get<IMeetupResponse>(this.END_POINT_MEETUPS_CREATED());
  }

  enrolledMeetups(): Observable<IEnrollmentResponse> {
    return this.http.get<IEnrollmentResponse>(this.END_POINT_MEETUPS_ENROLLMENTS());
  }
}
