import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { ISignInRequest, ISignUpRequest } from 'src/app/shared/models/auth';
import { IUser } from 'src/app/shared/models/user';
import { environment } from 'src/environments/environment';

import { getCurrentUser, removeCurrentUser, setCurrentUser } from '../storage';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  currentUser: Observable<IUser>;
  private currentUserSubject: BehaviorSubject<IUser>;

  constructor(private httpClient: HttpClient, private router: Router) {
    this.currentUserSubject = new BehaviorSubject<IUser>(getCurrentUser());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  get currentUserValue(): IUser {
    return this.currentUserSubject.value;
  }

  set currentUserValue(user: IUser) {
    setCurrentUser(user);
    this.currentUserSubject.next(user);
  }

  signUp(body: ISignUpRequest): Observable<IUser> {
    return this.httpClient.post<IUser>(`${environment.API_URL}/auth/sign-up`, body);
  }

  signIn(body: ISignInRequest): Observable<IUser> {
    return this.httpClient.post<IUser>(`${environment.API_URL}/auth/sign-in`, body);
  }

  signOut() {
    removeCurrentUser();
    this.currentUserSubject.next(null);
    this.router.navigate(['/signin']);
  }
}
