import { Component, OnInit } from '@angular/core';
import {QaService} from "../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {AllPermitDetailsDto, ConsolidatedInvoiceDto, PermitEntityDto} from "../../core/store/data/qa/qa.model";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";

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
  public myForm: FormGroup;


  constructor(
      private qaService: QaService,
      private router: Router,
      private _formBuilder: FormBuilder,
  ) { }

  ngOnInit() {
    this.initModelForm();
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

  initModelForm(): FormGroup{
    return this._formBuilder.group({
      otherControls: [''],
      // The formArray, empty
      myChoices: new FormArray([]),
    })
  }


  onCheckChange(event) {
    const formArray: FormArray = this.myForm.get('myChoices') as FormArray;

    /* Selected */
    if(event.target.checked){
      // Add a new control in the arrayForm
      formArray.push(new FormControl(event.target.value));
    }
    /* unselected */
    else{
      // find the unselected element
      let i: number = 0;

      formArray.controls.forEach((ctrl: FormControl) => {
        if(ctrl.value == event.target.value) {
          // Remove the unselected element from the arrayForm
          formArray.removeAt(i);
          return;
        }

        i++;
      });
    }
    console.log(formArray)
}
}
