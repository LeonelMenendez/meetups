import { Component, OnInit } from '@angular/core';
import { MeetupService } from 'src/app/core/services/meetup.service';

import { MeetupsDataSource } from '../../data-sources/meetups.data-source';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss'],
})
export class UserHomeComponent implements OnInit {
  displayedColumns = ['day', 'owner', 'temperature'];
  meetupsDataSource: MeetupsDataSource;

  constructor(private meetupService: MeetupService) {}

  ngOnInit(): void {
    this.meetupsDataSource = new MeetupsDataSource(this.meetupService);
    this.meetupsDataSource.loadEnrolledMeetups();
  }
}
