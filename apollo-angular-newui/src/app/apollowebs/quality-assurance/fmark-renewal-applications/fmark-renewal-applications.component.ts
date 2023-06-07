import {Component, OnInit} from '@angular/core';
import {PermitEntityDto} from '../../../core/store/data/qa/qa.model';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[][];
}

declare const $: any;

@Component({
  selector: 'app-fmark-renewal-applications',
  templateUrl: './fmark-renewal-applications.component.html',
  styleUrls: ['./fmark-renewal-applications.component.css']
})
export class FmarkRenewalApplicationsComponent implements OnInit {

  public dataTable: DataTable;
  public allPermitData: PermitEntityDto[];
  draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
  fmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID);

  constructor(
      private qaService: QaService,
      private router: Router,
  ) {

  }

  ngOnInit() {
    let formattedArray = [];
    this.qaService.loadPermitAwardedList(this.fmarkID).subscribe(
        (data: any) => {

          this.allPermitData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id]);

          this.dataTable = {
            // tslint:disable-next-line:max-line-length
            headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            dataRows: formattedArray


            // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

          };
        });
    // console.log(this.dataTable);
    // this.allPermitData = this.Object.json().results;
    // console.log(formattedArray);


    //
  }

  ngAfterViewInit() {
    $('#datatables').DataTable({
      'pagingType': 'full_numbers',
      'lengthMenu': [
        [10, 25, 50, -1],
        [10, 25, 50, 'All']
      ],
      responsive: true,
      language: {
        search: '_INPUT_',
        searchPlaceholder: 'Search records',
      }
    });
    let table: any;
    table = $(`#datatables`).DataTable();

  }

  onSelect(rowElement: string) {
    this.router.navigate(['/smarkpermitdetails'], {fragment: rowElement});
  }
}
