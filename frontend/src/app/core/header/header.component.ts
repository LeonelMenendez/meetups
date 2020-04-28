import { Component } from '@angular/core';
import { Role } from 'src/app/shared/enums/role';

import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  Role = Role;
  toggleNavbar = true;

  constructor(private authService: AuthService) {}

  signOut() {
    this.authService.signOut();
  }
}
