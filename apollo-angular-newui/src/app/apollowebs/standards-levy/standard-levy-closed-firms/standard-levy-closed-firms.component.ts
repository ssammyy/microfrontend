import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {CompanyModel} from "../../../core/store/data/levy/levy.model";
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
  selector: 'app-standard-levy-closed-firms',
  templateUrl: './standard-levy-closed-firms.component.html',
  styleUrls: ['./standard-levy-closed-firms.component.css']
})
export class StandardLevyClosedFirmsComponent implements OnInit {

  companyModels: CompanyModel[]=[];
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
  startDate: any = '';
  endDate: any = '';
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
      startDate:'',
      endDate : '',
      businessLines : '',
      region : '',
    });
    this.getBusinessLineList();
    this.getRegionList();
    this.getClosedFirms();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getClosedFirms(): void{
    this.loadingText='Loading Closed Firms...';
    this.SpinnerService.show();
    this.levyService.getClosedFirms().subscribe(
        (response: CompanyModel[])=> {
          this.companyModels = response;
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
    if (this.filterFormGroup.get("startDate").value == '' &&
        this.filterFormGroup.get("endDate").value == '' &&
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
      if (this.filterFormGroup.get("businessLines").value != '') {
        const businessLines = parseInt(this.filterFormGroup.get("businessLines").value);
        const businessLineName = this.regions.find(x => x.id == businessLines).region;
        this.arr.push("Department: " + businessLineName)
      }
      if (this.filterFormGroup.get("startDate").value != '') {
        this.arr.push("Start Date: " + this.formatFormDate(this.filterFormGroup.get("startDate").value)
            + " To " + this.formatFormDate(this.filterFormGroup.get("startDate").value))

      }

      this.levyService.getClosedFirmsFilter(this.filterFormGroup.value).subscribe(
          (response: CompanyModel[]) => {
            this.companyModels = response;
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
    this.getClosedFirms();
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
    // console.log(dateRangeStart.value);
    // console.log(dateRangeEnd.value);
    this.startDate = dateRangeStart.value
    this.endDate = dateRangeEnd.value

  }

}
