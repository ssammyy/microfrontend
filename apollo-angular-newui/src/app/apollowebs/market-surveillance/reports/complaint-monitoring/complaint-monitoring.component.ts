import {Component, OnInit, QueryList} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Subject} from "rxjs";
import {
    ApiResponseModel,
    ComplaintsInvestigationListDto, ComplaintViewSearchValues,
    MsDepartment, MsDivisionDetails,
    MsUsersDto
} from "../../../../core/store/data/ms/ms.model";
import {RegionsEntityDto} from "../../../../shared/models/master-data-details";
import {LocalDataSource} from "ng2-smart-table";
import {DataTableDirective} from "angular-datatables";




@Component({
  selector: 'app-complaint-monitoring',
  templateUrl: './complaint-monitoring.component.html',
  styleUrls: ['./complaint-monitoring.component.css']
})
export class ComplaintMonitoringComponent implements OnInit {
  activeStatus = 'consumer-complaints-report';
  searchFormGroup!: FormGroup;
  dtOptions: DataTables.Settings = {};
  dtTrigger1: Subject<any> = new Subject<any>();
  loadedData!: ComplaintsInvestigationListDto[];
  personalTasks = 'false';
  totalCount = 12;
  dataSet: LocalDataSource = new LocalDataSource();
  msOfficerLists!: MsUsersDto[];
  msRegions: RegionsEntityDto[] = [];
  msDepartments: MsDepartment[] = [];
  msDivisions: MsDivisionDetails[] = [];
  dtElements: QueryList<DataTableDirective>;
    defaultPage = 0;
    currentPage = 0;
    defaultPageSize = 1000;
    endPointStatusValue = 'complaint';
    searchTypeValue = 'COMPLAINT_MONITORING';
    searchStatus: any;
    message: any;
    departmentSelected: 0;
    submitted = false;
    complaintViewSearchValues: ComplaintViewSearchValues;

  constructor(
      private SpinnerService: NgxSpinnerService,
      private formBuilder: FormBuilder,
      private msService: MsService,


  ) {

  }

  ngOnInit(): void {
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip',
    };

  }

  toggle(statusField: string, endPointStatus: string, searchType: string): void {
      this.message = null;
      this.searchStatus =null;
    if (statusField !== this.activeStatus){
        this.activeStatus = statusField;
        this.endPointStatusValue = endPointStatus;
        this.searchTypeValue = searchType;

    }

  }

    // rerender(): void {
    //     this.dtElements.forEach((dtElement: DataTableDirective) => {
    //         if (dtElement.dtInstance)
    //             dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
    //                 dtInstance.destroy();
    //             });
    //     });
    //     setTimeout(() => {
    //         this.dtTrigger1.next();
    //         // this.dtTrigger2.next();
    //         // this.dtTrigger3.next();
    //     });
    //
    // }



    onChangeSelectedDepartment() {
        this.departmentSelected = this.searchFormGroup?.get('complaintDepartment')?.value;
    }

    // onSubmitSearch() {
    //     this.SpinnerService.show();
    //     this.submitted = true;
    //     this.complaintViewSearchValues = this.searchFormGroup.value;
    //     // tslint:disable-next-line:max-line-length
    //     this.msService.loadSearchComplaintViewList(String(this.defaultPage), String(this.defaultPageSize), this.complaintViewSearchValues, this.searchTypeValue).subscribe(
    //         (data: ApiResponseModel) => {
    //             if (data.responseCode === '00') {
    //                 this.loadedData = data.data;
    //                 this.totalCount = this.loadedData.length;
    //                 this.dataSet.load(this.loadedData);
    //                 this.rerender();
    //             }
    //             this.SpinnerService.hide();
    //         },
    //         error => {
    //             this.SpinnerService.hide();
    //             console.log(error);
    //         },
    //     );
    // }

    clearSearch() {
        this.searchFormGroup.reset();
        this.submitted = false;
    }
}
