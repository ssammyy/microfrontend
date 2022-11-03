import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {CompanyModel} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {RegionDto, ReportsPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";
import {BusinessLinesView, RegionView, UsersEntity} from "../../../core/store/data/std/std.model";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {Moment} from "moment";
import {OverlayService} from "../../../shared/loader/overlay.service";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-standard-levy-registered-firms',
  templateUrl: './standard-levy-registered-firms.component.html',
  styleUrls: ['./standard-levy-registered-firms.component.css']
})
export class StandardLevyRegisteredFirmsComponent implements OnInit {

  companyModels: CompanyModel[]=[];
  businessLines !: BusinessLinesView[];
  regions !: RegionView[];
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  loadingText: string;
  blob: Blob;
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
      this.today = new Date();
      this.filterApplied = false
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip'
    };
    this.getRegisteredFirms();
      this.filterFormGroup = this.formBuilder.group({
          startDate:'',
          endDate : '',
          businessLines : '',
          region : '',


      });
      this.loadAllRegions();
      this.getBusinessLineList();
      this.getRegionList();

  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getRegisteredFirms(): void{
    this.loadingText='Loading Registered Firms...';
    this.SpinnerService.show();
    this.levyService.getRegisteredFirms().subscribe(
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

            this.levyService.applyRegisteredFirmsFilter(this.filterFormGroup.value).subscribe(
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
        this.getRegisteredFirms();
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

  levyRegisteredFirmsReport(id: number, fileName: string, applicationType: string): void {
    this.loadingText = "Generating Report ...."
    this.SpinnerService.show();
    this.levyService.levyRegisteredFirmsReport(id).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.levyService.showError('AN ERROR OCCURRED');
        },
    );
  }

    public loadAllRegions(): void {
        this.levyService.loadRegionList().subscribe(
            (data: any) => {
                this.regions = data;
            }
        );
    }

}
