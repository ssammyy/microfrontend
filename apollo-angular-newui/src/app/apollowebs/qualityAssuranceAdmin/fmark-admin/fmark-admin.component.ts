import {Component, OnInit, ViewChild} from '@angular/core';
import * as CryptoJS from 'crypto-js';
import {MyTasksPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {Store} from "@ngrx/store";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {Router} from "@angular/router";
import {selectUserInfo} from "../../../core/store";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";

@Component({
  selector: 'app-fmark-admin',
  templateUrl: './fmark-admin.component.html',
  styleUrls: ['./fmark-admin.component.css']
})
export class FmarkAdminComponent implements OnInit {
  public allPermitTaskData: MyTasksPermitEntityDto[];

  roles: string[];

  internalUser: boolean;
  draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
  fmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
  dmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
  smarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  dataSource!: MatTableDataSource<MyTasksPermitEntityDto>;
  dataSourceB!: MatTableDataSource<MyTasksPermitEntityDto>;
  dataSourceC!: MatTableDataSource<MyTasksPermitEntityDto>;
  dataSourceD!: MatTableDataSource<MyTasksPermitEntityDto>;

  displayedColumns: string[] = ['permitRefNumber', 'createdOn', 'productName', 'tradeMark', 'sectionValue', 'permitStatus', 'actions'];


  constructor(private store$: Store<any>,
              private SpinnerService: NgxSpinnerService,
              private internalService: QaInternalService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.roles = u.roles;
      return this.roles = u.roles;
    });
    this.getMyFmarkTasks();
  }

  id: any = "My Tasks";

  tabChange(ids: any) {
    this.id = ids;
    console.log(this.id);
  }

  public getMyFmarkTasks(): void {
    this.SpinnerService.show();
    this.internalService.loadMyTasksByPermitType(this.fmarkID).subscribe(
        (dataResponse: ApiResponseModel) => {
          if (dataResponse.responseCode === '00') {
            // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
            this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];
            this.dataSource = new MatTableDataSource(this.allPermitTaskData);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          }
          this.SpinnerService.hide();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
        },
    );
  }


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.dataSourceB.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    if (this.dataSourceB.paginator) {
      this.dataSourceB.paginator.firstPage();
    }

  }


  gotoPermitDetails(permitId:string) {

    var text = permitId;
    var key = '11A1764225B11AA1';
    text = CryptoJS.enc.Utf8.parse(text);
    key = CryptoJS.enc.Utf8.parse(key);
    var encrypted = CryptoJS.AES.encrypt(text, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
    encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
    this.router.navigate(['/permit-details-admin',encrypted])


  }
}
