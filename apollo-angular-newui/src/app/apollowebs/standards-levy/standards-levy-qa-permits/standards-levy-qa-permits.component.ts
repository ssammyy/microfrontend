import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ReportsPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {CompanyModel, PenaltyDetails, QAPermitsDto} from "../../../core/store/data/levy/levy.model";
import {HttpErrorResponse} from "@angular/common/http";
import {DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import {formatDate} from "@angular/common";
import {RegionView} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {Moment} from "moment";
import swal from "sweetalert2";
import {OverlayService} from "../../../shared/loader/overlay.service";
export const PICK_FORMATS = {
  parse: {dateInput: {month: 'short', year: 'numeric', day: 'numeric'}},
  display: {
    dateInput: 'input',
    monthYearLabel: {year: 'numeric', month: 'short'},
    dateA11yLabel: {year: 'numeric', month: 'long', day: 'numeric'},
    monthYearA11yLabel: {year: 'numeric', month: 'long'}
  }
};
class PickDateAdapter extends NativeDateAdapter {
  format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'input') {
      return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss", this.locale);
    } else {
      return date.toDateString();
    }
  }
}
@Component({
  selector: 'app-standards-levy-qa-permits',
  templateUrl: './standards-levy-qa-permits.component.html',
  styleUrls: ['./standards-levy-qa-permits.component.css'],
  providers: [
    {provide: DateAdapter, useClass: PickDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: PICK_FORMATS}
  ]
})
export class StandardsLevyQaPermitsComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  public allSMarkPermitDatas: QAPermitsDto[] = [];
  loadingText: string;
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
  startDate: any = '';
  endDate: any = '';
  @ViewChild('dateRangeStart') startDateRef: ElementRef;
  @ViewChild('dateRangeEnd') endDateRef: ElementRef;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
      private formBuilder: FormBuilder,
      private spinnerService: OverlayService,
  ) { }

  ngOnInit(): void {
    this.today = new Date();
    this.filterApplied = false
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip'
    };
    this.filterFormGroup = this.formBuilder.group({
      startDate:'',
      endDate : '',
      region : ''
    });
    this.loadPermitGrantedReports();
    this.loadAllRegions();
    this.getRegionList();
  }

  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }

  public loadPermitGrantedReports(): void{
    this.loadingText = "Retrieving Data ...."
    this.SpinnerService.show();
    this.levyService.loadPermitGrantedReports().subscribe(
        (response: QAPermitsDto[]) => {
          this.allSMarkPermitDatas = response;
          //console.log(this.allSMarkPermitDatas);
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

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

  public applyPermitsFirmsFilter(formDirective): void {
    this.spinnerService.show("Filtering Data")
    this.error = false;
    //let us do validation
    if (this.filterFormGroup.get("startDate").value == '' &&
        this.filterFormGroup.get("endDate").value == '' &&
        this.filterFormGroup.get("region").value == ''
    ) {
      this.error = true;
      this.spinnerService.hide();
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
    if (this.filterFormGroup.get("startDate").value != '') {
      if (this.filterFormGroup.get("endDate").value == '') {
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
      if (this.filterFormGroup.get("startDate").value != '') {
        this.arr.push("Start Date: " + this.formatFormDate(this.filterFormGroup.get("startDate").value)
            + " To " + this.formatFormDate(this.filterFormGroup.get("startDate").value))

      }

      this.levyService.loadPermitGrantedReportsFilter(this.filterFormGroup.value).subscribe(
          (response: QAPermitsDto[]) => {
            this.allSMarkPermitDatas = response;
            this.spinnerService.hide();
            this.rerender();
            this.SpinnerService.hide();
            this.resetForm();
            this.filterApplied = true


          },
          (error: HttpErrorResponse) => {
            alert(error.message);
            this.SpinnerService.hide();
            this.spinnerService.hide();
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
    this.loadPermitGrantedReports();
  }
  formInit() {
    this.filterFormGroup = this.formBuilder.group({
      startDate:'',
      endDate : '',
      businessLines : '',
      region : ''

    });
  }
  dateRangeChange(dateRangeStart: HTMLInputElement, dateRangeEnd: HTMLInputElement) {
    //console.log(dateRangeStart.value);
    // console.log(dateRangeEnd.value);
    this.startDate = dateRangeStart.value
    this.endDate = dateRangeEnd.value

  }
  public loadAllRegions(): void {
    this.levyService.loadRegionList().subscribe(
        (data: any) => {
          this.regions = data;
        }
    );
  }



}
