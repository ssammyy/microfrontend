import {Component, OnInit} from '@angular/core';
import {faArrowRight} from '@fortawesome/free-solid-svg-icons/faArrowRight';
import {Location} from '@angular/common';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';


@Component({
  selector: 'app-ms-activities',
  templateUrl: './ms-activities.component.html',
  styleUrls: ['./ms-activities.component.css']
})
export class MsActivitiesComponent implements OnInit {

  arrowRightIcon = faArrowRight;
  arrowLeftIcon = faArrowLeft;



  constructor(private location: Location) { }

  ngOnInit(): void {
  }

  public backClicked(): void {
    this.location.back();
  }

}
