import {Component, OnInit} from '@angular/core';
import {PermitEntityDto} from '../../../core/store/data/qa/qa.model';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {LoadingService} from '../../../core/services/loader/loadingservice.service';
import {NgxSpinnerService} from 'ngx-spinner';

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[];
}

declare const $: any;
@Component({
  selector: 'app-dmark-expired-applications',
  templateUrl: './dmark-expired-applications.component.html',
  styleUrls: ['./dmark-expired-applications.component.css']
})
export class DmarkExpiredApplicationsComponent implements OnInit {

  public dataTable: DataTable;
  public allPermitData: PermitEntityDto[];

  // public formattedArray: any[];
  draftID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID;
  dmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;


  constructor(
      private qaService: QaService,
      private router: Router,
      private _loading: LoadingService,
      private SpinnerService: NgxSpinnerService
  ) {

  }

  ngOnInit() {
    let formattedArray = [];
    this.SpinnerService.show();
    this.qaService.loadPermitAwardedList(String(this.dmarkID)).subscribe(
        (data: any) => {

          this.allPermitData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id, i.processStatusID]);

          this.dataTable = {
            headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            dataRows: formattedArray
          };
          this.SpinnerService.hide();
        });
    // console.log(this.dataTable);
    // this.allPermitData = this.Object.json().results;
    // console.log(formattedArray);


    //
  }

  ngAfterViewInit() {
    $('#datatablesd').DataTable({
      'pagingType': 'full_numbers',

      responsive: true,
      language: {
        search: '_INPUT_',
        searchPlaceholder: 'Search records',
      }
    });
    let table: any;
    table = $(`#datatables`).DataTable();

  }

  onSelect(rowElement: string, processStatusID: number) {
    if (this.draftID === processStatusID) {
      this.router.navigate(['/dmark/newDmarkPermit'], {fragment: rowElement});
    } else {
      this.router.navigate(['/permitdetails'], {fragment: rowElement});
    }
  }
}
