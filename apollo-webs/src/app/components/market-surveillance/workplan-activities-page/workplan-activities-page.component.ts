import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SurveillanceWorkPlanData} from '../../../shared/mockData/surveillanceWorkplanData';
import {FetchWorkplanDataService} from '../../../shared/services/fetch-workplan-data.service';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {Location} from '@angular/common';

@Component({
  selector: 'app-workplan-activities-page',
  templateUrl: './workplan-activities-page.component.html',
  styleUrls: ['./workplan-activities-page.component.css']
})
export class WorkplanActivitiesPageComponent implements OnInit {

  arrowLeftIcon = faArrowLeft;
  selectedWorkplan?: SurveillanceWorkPlanData;

  constructor(
    private route: ActivatedRoute,
    public fetchWorkplanDataService: FetchWorkplanDataService,
    private location: Location
   ) {
  }

  ngOnInit(): void {
    this.getSelectedWorkplan();
  }

  getSelectedWorkplan(): void {
    const refNumber = this.route.snapshot.paramMap.get('refNumber') ?? '';
    this.fetchWorkplanDataService.getSelectedWorkplan(refNumber)
      .subscribe(workplan => this.selectedWorkplan = workplan);
  }

  public backClicked(): void {
    this.location.back();
  }

}
