import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MyTasksPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {SchemeMembershipService} from "../../../core/store/data/std/scheme-membership.service";
import {Router} from "@angular/router";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {NgxSpinnerService} from "ngx-spinner";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import * as CryptoJS from 'crypto-js';

@Component({
  selector: 'app-search-permits',
  templateUrl: './search-permits.component.html',
  styleUrls: ['./search-permits.component.css']
})
export class SearchPermitsComponent implements OnInit {
  public allPermitTaskData: MyTasksPermitEntityDto[];

  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();

  loading = false;
  loadingText: string;
  displayUsers: boolean = false;
  smarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;

  public itemId: string = "";

  constructor(private schemeMembershipService: SchemeMembershipService,
              private router: Router,
              private internalService: QaInternalService,
              private SpinnerService: NgxSpinnerService,
  ) {
  }

  ngOnInit(): void {

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
