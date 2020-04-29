import { Component } from '@angular/core';
import { Role } from 'src/app/shared/enums/role';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  Role = Role;
}
