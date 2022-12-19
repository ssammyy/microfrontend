import {Component, OnInit, ViewChild} from '@angular/core';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {
  GenerateInvoiceDto,
  GenerateInvoiceWithWithholdingDto,
  PermitInvoiceDto
} from '../../../core/store/data/qa/qa.model';
import {FormArray, FormBuilder, FormGroup} from '@angular/forms';
import {Subject} from 'rxjs';
import swal from 'sweetalert2';
import Swal from 'sweetalert2';
import {NgxSpinnerService} from 'ngx-spinner';
import {DataTableDirective} from "angular-datatables";
import {HttpErrorResponse} from "@angular/common/http";

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[][];
}

declare const $: any;

@Component({
  selector: 'app-invoice-consolidate',
  templateUrl: './invoice-consolidate.component.html',
  styleUrls: ['./invoice-consolidate.component.css']
})
export class InvoiceConsolidateComponent implements OnInit {
  public dataTable: DataTable;
  public allInvoiceData: PermitInvoiceDto[];
  tasks: PermitInvoiceDto[] = [];

  // consolidatedInvoice: GenerateInvoiceDto;
  name: string;
  checkboxGroup: FormGroup;
  submittedValue: any;
  final_array = [];
  selected = [];
  messages = []
  isWithHolding = "false";
  // permitInvoicesIDS = [];
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  loadingText: string;

  constructor(
      private qaService: QaService,
      private router: Router,
      private fb: FormBuilder,
      private SpinnerService: NgxSpinnerService,
  ) {
  }

  ngOnInit() {
    this.checkboxGroup = this.fb.group({
     });
    const checkboxControl = (this.checkboxGroup.controls.checkboxes as FormArray);
    this.getSPCTasks()


  }

  onSelect(rowElement: string) {
    this.router.navigate(['/invoiceDetails'], {fragment: rowElement});
  }



  // check if the item are selected
  checked(item) {
    if (this.selected.indexOf(item) !== -1) {
      return true;
    }
  }

  // when checkbox change, add/remove the item from the array
  onChange(checked, item) {
    if (checked) {
      this.selected.push(item);
    } else {
      this.selected.splice(item, 1);
      // console.log(      this.selected.splice(item, 1))
    }
  }

  setWithHoldingToTrue(checked) {
    if(checked) {
      this.isWithHolding = "true";
    }
  }



  payNowForOneInvoice(invoicesID: any, permitRefNumber: any) {

    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });

    swalWithBootstrapButtons.fire({
      title: 'Are you sure you want to pay for only this invoice?',
      text: 'You won\'t be able to make changes after submission!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes!',
      cancelButtonText: 'No!',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.SpinnerService.show();
        const permitInvoicesIDS: number[] = [];
        permitInvoicesIDS.push(invoicesID);
        const consolidatedInvoice = new GenerateInvoiceWithWithholdingDto;
        consolidatedInvoice.batchID = null;
        consolidatedInvoice.plantID = null;
        consolidatedInvoice.permitRefNumber = permitRefNumber;
        consolidatedInvoice.permitInvoicesID = permitInvoicesIDS;
        consolidatedInvoice.isWithHolding = this.isWithHolding

        console.log( consolidatedInvoice.isWithHolding)
        this.qaService.createInvoiceConsolidatedDetails(consolidatedInvoice).subscribe(
            (data) => {
              console.log(data);
              this.SpinnerService.hide();
              swal.fire({
                title: 'INVOICE GENERATED SUCCESSFULLY!, PROCEED TO PAY',
                buttonsStyling: false,
                customClass: {
                  confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'success'
              });
              this.router.navigate(['/invoiceDetails'], {fragment: String(data.batchDetails.batchID)});
            },
        );
      } else if (
          /* Read more about handling dismissals below */
          result.dismiss === swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire(
            'Cancelled',
            'You can click the \'On the side Check Box for Consolidating\' more than one invoice.',
            'error'
        );
      }
    });
  }

  submit() {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    });

    swalWithBootstrapButtons.fire({
      title: 'Are you sure you want to pay for the selected invoice\'s?',
      text: 'You won\'t be able to make changes after submission!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes!',
      cancelButtonText: 'No!',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.SpinnerService.show();
        this.final_array.push(this.selected.sort());
        console.log(this.final_array);
        // const permitInvoicesIDS:number[] = this.final_array;
        const permitInvoicesIDS: number[] = [];
        this.final_array.forEach(function (dataValue) {
          for (let i = 0; i <= dataValue.length - 1; i++) {
            const pickedI = dataValue[i];
            const idIndex = dataValue[i].length;
            console.log(`VALUE OF I =${dataValue[i]}`);
            const myData = dataValue[i];
            console.log(`DATA ADDED ${myData}`);
            permitInvoicesIDS.push(myData);
          }
        });
        console.log(permitInvoicesIDS);
        const consolidatedInvoice = new GenerateInvoiceDto;
        consolidatedInvoice.batchID = null;
        consolidatedInvoice.plantID = null;
        consolidatedInvoice.permitRefNumber = null;
        consolidatedInvoice.permitInvoicesID = permitInvoicesIDS;
        console.log('TEST CONSOLIDATE' + consolidatedInvoice);
        console.log(consolidatedInvoice.permitInvoicesID);
        this.qaService.createInvoiceConsolidatedDetails(consolidatedInvoice).subscribe(
            (data) => {
              this.SpinnerService.hide();
              swal.fire({
                title: 'INVOICE CONSOLIDATED SUCCESSFULLY!',
                buttonsStyling: false,
                customClass: {
                  confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'success'
              });
              this.router.navigate(['/invoiceDetails'], {fragment: String(data.batchDetails.batchID)});
            },
        );
      } else if (
          /* Read more about handling dismissals below */
          result.dismiss === swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire(
            'Cancelled',
            'You can click the \'On the side Check Box for Consolidating\' more than one invoice.',
            'error'
        );
      }
        }
    );


  }


  public getSPCTasks(): void {
    this.loadingText = "Retrieving Invoices Please Wait ...."

    this.SpinnerService.show();
    this.qaService.loadInvoiceListWithNoBatchID().subscribe(
        (response: PermitInvoiceDto[]) => {
          this.SpinnerService.hide();
          this.tasks = response;

          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide();
        }
    );
  }

}
