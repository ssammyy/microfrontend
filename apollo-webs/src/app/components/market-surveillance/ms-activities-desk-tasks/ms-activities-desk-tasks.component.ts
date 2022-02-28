import {Component, OnInit} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {Location} from '@angular/common';
import {faFilePdf} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-ms-activities-desk-tasks',
  templateUrl: './ms-activities-desk-tasks.component.html',
  styleUrls: ['./ms-activities-desk-tasks.component.css']
})
export class MsActivitiesDeskTasksComponent implements OnInit {

  arrowLeftIcon = faArrowLeft;
  pdfFileIcon = faFilePdf;
  searchText = '';

  standards = [
    {
      ksNumber: 'KS 815:2018',
      ksTitle: 'Retro-reflective and fluorescent warning triangles for motor vehicles - Specification',
      icsNumber: 'ICS 423: 2015'
    },
    {
      ksNumber: 'KS 820:2018',
      ksTitle: 'Retro-reflective and fluorescent warning triangles for motor vehicles',
      icsNumber: 'ICS 423: 2015'
    }
  ];


  constructor(private location: Location) {
  }

  ngOnInit(): void {
  }

  public backClicked(): void {
    this.location.back();
  }


}
