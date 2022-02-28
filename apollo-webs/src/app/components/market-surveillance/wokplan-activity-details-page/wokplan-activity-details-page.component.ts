import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FetchWorkplanDataService} from '../../../shared/services/fetch-workplan-data.service';
import {Activity} from '../../../shared/mockData/surveillanceWorkplanData';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {Location} from '@angular/common';

@Component({
  selector: 'app-wokplan-activity-details-page',
  templateUrl: './wokplan-activity-details-page.component.html',
  styleUrls: ['./wokplan-activity-details-page.component.css']
})
export class WokplanActivityDetailsPageComponent implements OnInit {

  activity?: Activity;
  arrowLeftIcon = faArrowLeft;


  constructor(private route: ActivatedRoute,
              private fetchWorkplanDataService: FetchWorkplanDataService,
              private location: Location) {
  }

  ngOnInit(): void {
    this.getSelectedActivity();
  }

  getSelectedActivity(): void {
    const activityId = this.route.snapshot.paramMap.get('activityId') ?? '';
    this.fetchWorkplanDataService.getActivity(activityId)
      .subscribe( (activity) => {
        this.activity = activity;
        console.log(activity);
      });
  }

  public getResources(resources: any): any {
    return resources.map((a: any, _: any) => {
      return a.resourceName;
    }).join(', ');
  }

  public backClicked(): void {
    this.location.back();
  }

}
