import { Component, OnInit } from '@angular/core';
import {DatePipe} from "@angular/common";
import {ConsignmentStatusComponent} from "../../../../core/shared/customs/consignment-status/consignment-status.component";
import {LocalDataSource} from "ng2-smart-table";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
  selector: 'app-view-bill-limits',
  templateUrl: './view-bill-limits.component.html',
  styleUrls: ['./view-bill-limits.component.css']
})
export class ViewBillLimitsComponent implements OnInit {
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
        //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
        // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
      ],
      position: 'right' // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true
    },
    noDataMessage: 'No data found',
    columns: {
      corporateType: {
        title: 'Corportate Type',
        type: 'string',
        filter: false
      },
      billType: {
        title: 'Billing Type',
        type: 'string',
        filter: false
      },
      billPaymentDay: {
        title: 'Payment Days',
        type: 'string',
      },
      billReceiptPrefix: {
        title: 'Receipt Prefix',
        type: 'string',
        filter: false
      },
      maxBillAmount: {
        title: 'Max Amount',
        type: 'string'
      },
      penaltyAmount: {
        title: 'Penalty',
        type: 'string'
      },
      penaltyType: {
        title: 'Penalty Type',
        type: 'string'
      },
      applicableDate: {
        title: 'Exchange Date',
        type: 'date',
        valuePrepareFunction: (date) => {
          if (date) {
            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
          }
          return ""
        },
      },
      status: {
        title: 'Status',
        type: 'custom',
        renderComponent: ConsignmentStatusComponent
      }
    },
    pager: {
      display: true,
      perPage: 20
    }
  };
  dataSet: LocalDataSource = new LocalDataSource();
  constructor(private fiService: FinanceInvoiceService) {
  }

  ngOnInit(): void {
    this.loadBillTypes()
  }


  loadBillTypes() {
    this.fiService.loadBillTypes()
        .subscribe(
            res => {
              if (res.responseCode == "00") {
                this.dataSet.load(res.data)
              } else {
                this.fiService.showError(res.message, null)
              }
            }
        )
  }
}
