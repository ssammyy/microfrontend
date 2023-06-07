import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {
  Activity,
  AllWorkPlanData,
  sampleWorkPlanData,
  SurveillanceWorkPlanData,
  SurveillanceWorkplanData
} from '../mockData/surveillanceWorkplanData';

@Injectable({
  providedIn: 'root'
})

export class FetchWorkplanDataService {


  constructor(private http: HttpClient) {
  }

  getActivity(id: any): Observable<Activity> {
    let sampleActivity: Observable<Activity>;
    // @ts-ignore
    const value = sampleWorkPlanData[0].activities.find((act) => act.activityId === id);
    // @ts-ignore
    sampleActivity = of(value);
    return sampleActivity;
  }

  getAllWorkplans(): Observable<AllWorkPlanData[]> {
    let allWorkplans: Observable<AllWorkPlanData[]>;
    allWorkplans = of(sampleWorkPlanData);
    return allWorkplans;
  }

  getSurveillanceWorkplans(): Observable<SurveillanceWorkPlanData[]> {
    let surveillanceWorkplans: Observable<SurveillanceWorkPlanData[]>;
    surveillanceWorkplans = of(SurveillanceWorkplanData);
    return surveillanceWorkplans;
  }

  getSelectedWorkplan(refNumber: string): Observable<SurveillanceWorkPlanData> {
    // @ts-ignore
    return of(sampleWorkPlanData.find(workplan => workplan.refNumber === refNumber));
  }


}
