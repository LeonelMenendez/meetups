import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ISignInRequest, ISignUpRequest } from 'src/app/shared/models/auth';
import { IUser } from 'src/app/shared/models/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient) {}

  signUp(body: ISignUpRequest): Observable<IUser> {
    return this.httpClient.post<IUser>(`${environment.API_URL}/auth/sign-up`, body);
  }

  signIn(body: ISignInRequest): Observable<IUser> {
    return this.httpClient.post<IUser>(`${environment.API_URL}/auth/sign-in`, body);
  }
}
