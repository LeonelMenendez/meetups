import { Component, OnInit } from '@angular/core';
import { NgxPermissionsService } from 'ngx-permissions';
import { AuthService } from 'src/app/core/services/auth.service';
import { getCurrentUser } from 'src/app/core/storage';
import { Role } from 'src/app/shared/enums/role';
import { IUser } from 'src/app/shared/models/user';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit {
  currentUser: IUser;

  constructor(private authService: AuthService, private permissionsService: NgxPermissionsService) {
    this.authService.currentUser.subscribe((currentUser) => (this.currentUser = currentUser));
  }

  ngOnInit(): void {
    const currentUser = getCurrentUser();
    if (currentUser) {
      const role = Role[currentUser.role];
      this.permissionsService.loadPermissions(Array.of(role));
    }
  }
}
