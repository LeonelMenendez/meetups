import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { EnrollmentService } from 'src/app/core/services/enrollment.service';
import { UserService } from 'src/app/core/services/user.service';
import { IEnrollmentResponse } from 'src/app/shared/models/enrollment';

import { EnrollmentsDataSource } from '../../../../shared/data-sources/enrollments.data-source';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss'],
})
export class UserHomeComponent implements OnInit {
  today = moment();
  displayedColumns = ['day', 'owner', 'temperature', 'checkIn'];
  enrollmentsDataSource: EnrollmentsDataSource;

  constructor(private userService: UserService, private enrollmentService: EnrollmentService) {}

  ngOnInit(): void {
    this.enrollmentsDataSource = new EnrollmentsDataSource();
    this.enrollmentsDataSource.load(this.userService);
  }

  canCheckIn(element: IEnrollmentResponse): boolean {
    return !element.checkedIn && moment(element.meetupDay).isSame(this.today, 'day');
  }

  checkIn(element: IEnrollmentResponse) {
    this.enrollmentService.checkIn(element.id).subscribe(() => {
      element.checkedIn = true;
      this.enrollmentsDataSource.update(element);
    });
  }
}
