import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from 'src/app/shared/enums/role';
import { IUser } from 'src/app/shared/models/user';
import { environment } from 'src/environments/environment';

import { RequestService } from './request.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private END_POINT_BASE = `${environment.API_URL}/users`;

  constructor(private http: HttpClient, private requestService: RequestService) {}

  findAll(role: Role): Observable<IUser[]> {
    const params = this.requestService.getHttpParams({ role });
    return this.http.get<IUser[]>(this.END_POINT_BASE, { params });
  }
}
