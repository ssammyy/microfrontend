import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {CompanyModel} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {RegionDto, ReportsPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {BusinessLinesView, RegionView, UsersEntity} from "../../../core/store/data/std/std.model";

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
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
      this.today = new Date();
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

    public applyRegisteredFirmsFilter(formDirective): void {
        this.SpinnerService.show("Filtering Data")
        this.error = false;
        //let us do validation
        if (this.filterFormGroup.get("startDate").value == '' &&
            this.filterFormGroup.get("endDate").value == '' &&
            this.filterFormGroup.get("businessLines").value == '' &&
            this.filterFormGroup.get("region").value == ''
        ) {
            this.error = true;
            this.SpinnerService.hide();

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
            if (this.filterFormGroup.get("endDate").value == null) {
                this.SpinnerService.hide();

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
            this.levyService.applyRegisteredFirmsFilter(this.filterFormGroup.value).subscribe(
                (response: CompanyModel[]) => {
                    this.SpinnerService.hide();
                    this.companyModels = response;
                    this.rerender()


                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.SpinnerService.hide();
                }
            );
        }


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
