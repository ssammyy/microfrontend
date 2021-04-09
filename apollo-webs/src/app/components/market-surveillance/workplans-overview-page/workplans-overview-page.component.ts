import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {
  sampleWorkPlanData,
  SurveillanceWorkPlanData,
  SurveillanceWorkplanData
} from '../../../shared/mockData/surveillanceWorkplanData';
import {FetchWorkplanDataService} from '../../../shared/services/fetch-workplan-data.service';

@Component({
  selector: 'app-workplans-overview-page',
  templateUrl: './workplans-overview-page.component.html',
  styleUrls: ['./workplans-overview-page.component.css']
})
export class WorkplansOverviewPageComponent implements OnInit {

  public surveillanceWorkPlanData = SurveillanceWorkplanData;
  selectedSurveillanceWorkPlan?: SurveillanceWorkPlanData;
  public allWorkPlanData = sampleWorkPlanData;
  public isLoading = false;
  public showModal = false;


  constructor(
    private router: Router,
    public fetchWorkplanDataService: FetchWorkplanDataService
  ) {
  }

  ngOnInit(): void {
    this.getAllWorkplans();
    this.getSurveillanceWorkplans();
  }

  getSurveillanceWorkplans(): void {
    this.fetchWorkplanDataService.getSurveillanceWorkplans()
      .subscribe(workplans => this.surveillanceWorkPlanData = workplans);
  }

  getAllWorkplans(): void {
    this.fetchWorkplanDataService.getAllWorkplans()
      .subscribe(allWorkplans => this.allWorkPlanData = allWorkplans);
  }

  public onclick(): void {

    if (!this.isLoading) {
      this.isLoading = true;
      setTimeout(() => {
        this.isLoading = false;
      }, 1000);
    }
  }

  public open(): void {
    if (0) {
      // Dont open the modal
      this.showModal = false;
    } else {
      // Open the modal
      this.showModal = true;
    }
  }
}
