import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {PaymentDetails} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {BusinessLinesView, RegionView} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {Moment} from "moment";
import {OverlayService} from "../../../shared/loader/overlay.service";
import {formatDate} from "@angular/common";
import swal from "sweetalert2";

@Component({
  selector: 'app-standard-levy-penalty-report',
  templateUrl: './standard-levy-penalty-report.component.html',
  styleUrls: ['./standard-levy-penalty-report.component.css']
})
export class StandardLevyPenaltyReportComponent implements OnInit {
  paymentDetails: PaymentDetails[]=[];
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  loadingText: string;
  businessLines !: BusinessLinesView[];
  regions !: RegionView[];
  filterFormGroup: FormGroup;
  today: any;
  error: boolean;
  arr: string[] = [];
  filterApplied: boolean;
  dateFormat = "yyyy-MM-dd";
  language = "en";
  @ViewChild('dateRangeInput ') dateRangeInput: MatDateRangePicker<Moment>;
  private pickerDR: MatDateRangePicker<Moment>;
  @ViewChild('formDirective') private formDirective: NgForm;
  periodFrom: any = '';
  periodTo: any = '';
  @ViewChild('dateRangeStart') startDateRef: ElementRef;
  @ViewChild('dateRangeEnd') endDateRef: ElementRef;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
      private formBuilder: FormBuilder,
      private spinnerService: OverlayService
  ) { }

  ngOnInit(): void {
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip'
    };
    this.today = new Date();
    this.filterApplied = false
    this.filterFormGroup = this.formBuilder.group({
      periodFrom:'',
      periodTo : '',
      businessLines : '',
      region : '',
    });
    this.getBusinessLineList();
    this.getRegionList();
    this.getPenaltyReport();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getPenaltyReport(): void{
    this.loadingText='Loading Penalty Report...';
    this.SpinnerService.show();
    this.levyService.getPenaltyReport().subscribe(
        (response: PaymentDetails[])=> {
          this.paymentDetails = response;
          console.log(this.paymentDetails);
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger1.next();
    });

  }
  public getBusinessLineList(): void {
    this.SpinnerService.show();
    this.levyService.getBusinessLineList().subscribe(
        (response: BusinessLinesView[]) => {
          this.SpinnerService.hide();
          this.businessLines = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getRegionList(): void {
    this.SpinnerService.show();
    this.levyService.getRegionList().subscribe(
        (response: RegionView[]) => {
          this.SpinnerService.hide();
          this.regions = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  formatFormDate(date: string) {
    return formatDate(date, this.dateFormat, this.language);
  }

  public applyRegisteredFirmsFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
    this.error = false;
    //let us do validation
    if (this.filterFormGroup.get("periodFrom").value == '' &&
        this.filterFormGroup.get("periodTo").value == '' &&
        this.filterFormGroup.get("businessLines").value == '' &&
        this.filterFormGroup.get("region").value == ''
    ) {
      this.error = true;
      this.spinnerService.hide();

      swal.fire({
        title: 'Please Select At Least One Filter.',
        buttonsStyling: false,
        customClass: {
          confirmButton: 'btn btn-warning form-wizard-next-btn ',
        },
        icon: 'warning'
      });
    }
    if (this.filterFormGroup.get("periodFrom").value != '') {
      if (this.filterFormGroup.get("periodTo").value == '') {
        this.spinnerService.hide();

        swal.fire({
          title: 'Please Select The End Date.',
          buttonsStyling: false,
          customClass: {
            confirmButton: 'btn btn-warning form-wizard-next-btn ',
          },
          icon: 'warning'
        });
        this.error = true;

      }

    }
    if (!this.error) {
      this.arr = []
      if (this.filterFormGroup.get("region").value != '') {
        const region = parseInt(this.filterFormGroup.get("region").value);
        const regionName = this.regions.find(x => x.id == region).region;
        this.arr.push("Region: " + regionName)
      }
      if (this.filterFormGroup.get("businessLines").value != '') {
        const businessLines = parseInt(this.filterFormGroup.get("businessLines").value);
        const businessLineName = this.regions.find(x => x.id == businessLines).region;
        this.arr.push("Department: " + businessLineName)
      }
      if (this.filterFormGroup.get("periodFrom").value != '') {
        this.arr.push("Period From: " + this.formatFormDate(this.filterFormGroup.get("periodFrom").value)
            + " To " + this.formatFormDate(this.filterFormGroup.get("periodFrom").value))

      }

      this.levyService.getPenaltyReportFilter(this.filterFormGroup.value).subscribe(
          (response: PaymentDetails[]) => {
            this.paymentDetails = response;
            this.spinnerService.hide();
            this.rerender();
            this.SpinnerService.hide();
            this.resetForm();
            this.filterApplied = true


          },
          (error: HttpErrorResponse) => {
            alert(error.message);
            this.SpinnerService.hide();
          }
      );
    }


  }

  resetForm() {
    this.filterFormGroup.reset()
    this.startDateRef.nativeElement.value = '';
    this.endDateRef.nativeElement.value = '';
    this.formInit()

  }
  clearFilter() {
    this.filterApplied = false
    this.getPenaltyReport();
  }
  formInit() {
    this.filterFormGroup = this.formBuilder.group({
      periodFrom:'',
      periodTo : '',
      businessLines : '',
      region : ''

    });
  }
  dateRangeChange(dateRangeStart: HTMLInputElement, dateRangeEnd: HTMLInputElement) {
    // console.log(dateRangeStart.value);
    // console.log(dateRangeEnd.value);
    this.periodFrom = dateRangeStart.value
    this.periodTo = dateRangeEnd.value

  }

}
