import { IUser } from '../shared/models/user';

const CURRENT_USER = 'currentUser';

export function getCurrentUser(): IUser {
  return JSON.parse(localStorage.getItem(CURRENT_USER));
}

export function setCurrentUser(user: IUser) {
  localStorage.setItem(CURRENT_USER, JSON.stringify(user));
}

export function removeCurrentUser() {
  localStorage.removeItem(CURRENT_USER);
}
