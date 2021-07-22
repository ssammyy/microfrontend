import { Component, OnInit } from '@angular/core';
import {QaService} from "../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {AllPermitDetailsDto, ConsolidatedInvoiceDto, PermitEntityDto} from "../../core/store/data/qa/qa.model";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";

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
  public allInvoiceData: ConsolidatedInvoiceDto[];
  name:string;
  checkboxGroup: FormGroup;
  submittedValue: any;
  messages = [];
  selected = [];


  constructor(
      private qaService: QaService,
      private router: Router,
      private fb: FormBuilder,
  ) { }

  ngOnInit() {
    this.checkboxGroup = this.fb.group({
     });
    const checkboxControl = (this.checkboxGroup.controls.checkboxes as FormArray);

    let formattedArray = [];
    this.qaService.loadInvoiceBatchList().subscribe(
        (data: any) => {
          this.allInvoiceData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.invoiceNumber, i.receiptNo, i.paidDate, i.totalAmount, i.paidStatus, i.id, i.batchID]);

          this.dataTable = {
            headerRow: ['Invoice No', 'Receipt No', 'Date', 'Total Amount', ' Select'],
            footerRow: ['Invoice No', 'Receipt No', 'Date', 'Total Amount', ' Select'],
            dataRows: formattedArray


            // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

          };

        }
    );

  }

  onSelect(rowElement: string) {
    this.router.navigate(['/invoiceDetails'], {fragment: rowElement});
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

    // Edit record
    table.on('click', '.edit', function (e) {
      let $tr = $(this).closest('tr');
      if ($($tr).hasClass('child')) {
        $tr = $tr.prev('.parent');
      }

      let data: any;
      data = table.row($tr).data();
      alert('You press on Row: ' + data[0] + ' ' + data[1] + ' ' + data[2] + '\'s row.');
      e.preventDefault();
    });

    // Delete a record
    table.on('click', '.remove', function (e) {
      const $tr = $(this).closest('tr');
      table.row($tr).remove().draw();
      e.preventDefault();
    });

    // Like record
    table.on('click', '.like', function (e) {
      alert('You clicked on Like button');
      e.preventDefault();
    });

    $('.card .material-datatables label').addClass('form-group');
  }


  // check if the item are selected
  checked(item){
    if(this.selected.indexOf(item) != -1){
      return true;
    }
  }

  // when checkbox change, add/remove the item from the array
  onChange(checked, item){
    if(checked){
      this.selected.push(item);
    } else {
      this.selected.splice(this.selected.indexOf(item), 1)
    }
  }

  submit() {
    this.messages.push(this.selected.sort());
  }

}
