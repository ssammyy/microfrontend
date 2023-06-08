import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-rfc-coc-documents',
  templateUrl: './rfc-coc-documents.component.html',
  styleUrls: ['./rfc-coc-documents.component.css']
})
export class RfcCocDocumentsComponent implements OnInit {

  dataSet: LocalDataSource = new LocalDataSource();
  message: any
  reviewStatus: number = 3
  activeStatus: any = 'under-review'
  keywords: string
  page: number = 0
  pvocPartners: any
  pageSize: number = 20;
  currentPageInternal: number = 0;
  totalCount: number;
  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
      ],
      position: 'right' // left|right
    },
    noDataMessage: 'No CORs found',
    columns: {
      rfcNumber: {
        title: 'RFC No.',
        type: 'string',
        filter: false
      },
      ucrNumber: {
        title: 'UCR No.',
        type: 'string',
        filter: false
      },
      idfNumber: {
        title: 'IDF No.',
        type: 'string',
      },
      importerName: {
        title: 'Importer Name',
        type: 'string',
        filter: false
      },
      exporterName: {
        title: 'Exporter Name',
        type: 'string'
      },
      rfcDate: {
        title: 'RFC Date',
        type: 'string',
      },
      countryOfSupply: {
        title: 'Country',
        type: 'string',
      },
      solReference: {
        title: 'SOL Ref.',
        type: 'string',
      },
      sorReference: {
        title: 'SOR Ref.',
        type: 'string',
      },
      route: {
        title: 'Route',
        type: 'string',
      },
      reviewStatus: {
        title: 'Review Status',
        type: 'string',
        filter: false
      },
      createdOn: {
        title: 'Date Received',
        type: 'date',
        valuePrepareFunction: (date) => {
          if (date) {
            try {
              return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
            } catch (e) {
              return date
            }
          }
          return "N/A"
        },
      },
    },
    pager: {
      display: true,
      perPage: 20
    }
  };

  constructor(private pvocService: PVOCService, private router: Router) {
  }

  ngOnInit(): void {
  }

  searchPhraseChanged() {
    this.loadData()
  }

  toggleStatus(status) {
    if (status === this.activeStatus) {
      return
    }
    this.activeStatus = status
    switch (status) {
      case "under-review":
        this.reviewStatus = 3
        break
      case "rejected-requests":
        this.reviewStatus = 2
        break
      case "approved-requests":
        this.reviewStatus = 1
        break
      default:
        this.reviewStatus = 0
    }
    this.loadData()
  }

  pageChange(pageIndex) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1
      this.page = pageIndex
      this.loadData()
    }
  }

  loadData() {
    this.pvocService.loadRfcForCocDocuments(this.keywords, this.reviewStatus, this.currentPageInternal, this.pageSize)
        .subscribe(
            res => {
              if (res.responseCode === "00") {
                this.dataSet.empty()
                this.dataSet.load(res.data)
                this.totalCount = res.totalCount
              } else {
                this.message = res.message
              }
            },
            error => {
              this.message = error.message
            }
        )
  }

  viewRfcCor(data: any) {
    this.router.navigate(["/pvoc/foreign/rfc/coc/", data.rfcId])
  }

  auctionEvent(actions) {
    switch (actions.action) {
      case 'viewRecord':
        this.viewRfcCor(actions.data)
        break
    }
  }

}
