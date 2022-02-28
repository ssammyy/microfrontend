import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MarketSurveillanceService} from '../../../shared/services/market-surveillance.service';
import {Complaints} from '../../../shared/models/complaints';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AlertService} from '../../../shared/services/alert.service';

@Component({
  selector: 'app-complaints-page',
  templateUrl: './complaints-page.component.html',
  styleUrls: ['./complaints-page.component.css']
})
export class ComplaintsPageComponent implements OnInit {

  p = 1;
  allComplaintsData: Complaints[] = [];
  searchFormGroup!: FormGroup;

  loading = false;
  submitted = false;

  constructor(
    private marketSurveillanceService: MarketSurveillanceService,
    private router: Router,
    private formBuilder: FormBuilder,
    private alertService: AlertService,
    private spinner: NgxSpinnerService,
  ) {
  }

  get formSearch(): any {
    return this.searchFormGroup.controls;
  }

  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      refNumber: ['', Validators.required],
      date: ['', null],
    });

    this.spinner.show();
    this.marketSurveillanceService.loadMSComplaintList().subscribe(
      (data: any) => {
        this.allComplaintsData = data;
        console.log(data);
      }
    );
  }

  public onSelect(refNumber: string): any {
    this.router.navigate(['/complaint-details'], {fragment: refNumber});
  }

  onSubmitSearch(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.searchFormGroup.invalid) {
      return;
    }
    this.loading = true;
    this.marketSurveillanceService.loadComplaintSearch(this.searchFormGroup.value).subscribe(
      (data: any) => {
        console.log(data);
        this.allComplaintsData = data;
        this.p = 1;
      },
      (error: { error: { message: any; }; }) => {
        this.alertService.error(error.error.message, 'Access Denied');
        // this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      });
  }
}
