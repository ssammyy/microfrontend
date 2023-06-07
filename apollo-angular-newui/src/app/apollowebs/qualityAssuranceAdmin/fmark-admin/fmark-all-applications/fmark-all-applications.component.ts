import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MyTasksPermitEntityDto} from "../../../../core/store/data/qa/qa.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {Router} from "@angular/router";
import {QaInternalService} from "../../../../core/store/data/qa/qa-internal.service";
import {NgxSpinnerService} from "ngx-spinner";
import {ApiResponseModel} from "../../../../core/store/data/ms/ms.model";
import * as CryptoJS from 'crypto-js';
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-fmark-all-applications',
  templateUrl: './fmark-all-applications.component.html',
  styleUrls: ['./fmark-all-applications.component.css']
})
export class FmarkAllApplicationsComponent implements OnInit {

  public allPermitTaskData: MyTasksPermitEntityDto[];
  roles: string[];

  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();

  loading = false;
  loadingText: string;
  displayUsers: boolean = false;
  fmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;

  public itemId: string = "";


  constructor(private schemeMembershipService: SchemeMembershipService,
              private router: Router,
              private internalService: QaInternalService,
              private SpinnerService: NgxSpinnerService,
              private store$: Store<any>,
  ) {
  }

  ngOnInit(): void {
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.roles = u.roles;
      return this.roles = u.roles;
    });
    this.getMySmarkComplete();

  }

  public getMySmarkComplete(): void {
    this.SpinnerService.show();
    this.internalService.loadMyAllPermitByPermitType(this.fmarkID).subscribe(
        (dataResponse: ApiResponseModel) => {
          if (dataResponse.responseCode === '00') {
            // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
            this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];
            this.rerender();
            this.SpinnerService.hide();
            this.displayUsers = true;

            this.loading = false;

          }
          this.SpinnerService.hide();
          this.SpinnerService.hide()
          this.displayUsers = true;

          this.loading = false;
        },
        error => {
          this.SpinnerService.hide();
          this.SpinnerService.hide()
          this.displayUsers = true;

          this.loading = false;
        },
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

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger1.unsubscribe();


  }

  gotoPermitDetails(permitId: string) {
    let text = permitId;
    let key = '11A1764225B11AA1';
    text = CryptoJS.enc.Utf8.parse(text);
    key = CryptoJS.enc.Utf8.parse(key);
    let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
    encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
    this.router.navigate(['/permit-details-admin', encrypted])
  }


}

