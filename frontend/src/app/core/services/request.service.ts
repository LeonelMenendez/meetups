import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class RequestService {
  constructor(private authService: AuthService) {}

  get userId(): string {
    return this.authService.currentUserValue.id.toString();
  }

  getHttpParams(params: object): HttpParams {
    let httpParams = new HttpParams();

    for (let [key, value] of (<any>Object).entries(params)) {
      httpParams = httpParams.append(key, value);
    }

    return httpParams;
  }
}
