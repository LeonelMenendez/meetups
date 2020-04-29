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
  private END_POINT_BASE = `${environment.API_URL}/auth`;
  private END_SIGN_UP = `${this.END_POINT_BASE}/sign-up`;
  private END_SIGN_IN = `${this.END_POINT_BASE}/sign-in`;

  currentUser: Observable<IUser>;
  private currentUserSubject: BehaviorSubject<IUser>;

  constructor(private http: HttpClient, private router: Router) {
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

  signUp(signUpRequest: ISignUpRequest): Observable<IUser> {
    return this.http.post<IUser>(this.END_SIGN_UP, signUpRequest);
  }

  signIn(signInRequest: ISignInRequest): Observable<IUser> {
    return this.http.post<IUser>(this.END_SIGN_IN, signInRequest);
  }

  signOut() {
    removeCurrentUser();
    this.currentUserSubject.next(null);
    this.router.navigate(['/signin']);
  }
}
