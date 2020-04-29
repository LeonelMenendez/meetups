import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InvitationStatus } from 'src/app/shared/enums/invitation-status';
import { IInvitationResponse } from 'src/app/shared/models/invitation';
import { environment } from 'src/environments/environment';

import { RequestService } from './request.service';

@Injectable({
  providedIn: 'root',
})
export class InvitationService {
  private END_POINT_BASE = `${environment.API_URL}/invitations`;

  constructor(private http: HttpClient, private requestService: RequestService) {}

  findAll(): Observable<IInvitationResponse[]> {
    const params = this.requestService.getHttpParams({ userId: this.requestService.userId });
    return this.http.get<IInvitationResponse[]>(this.END_POINT_BASE, { params });
  }

  changeStatus(invitationId: number, status: InvitationStatus): Observable<IInvitationResponse> {
    return this.http.patch<IInvitationResponse>(`${this.END_POINT_BASE}/${invitationId}`, {
      status,
    });
  }
}
