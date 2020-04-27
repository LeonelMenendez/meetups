import { Component } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { IUser } from 'src/app/shared/models/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  currentUser: IUser;

  constructor(private authService: AuthService) {
    this.authService.currentUser.subscribe((currentUser) => (this.currentUser = currentUser));
  }
}
