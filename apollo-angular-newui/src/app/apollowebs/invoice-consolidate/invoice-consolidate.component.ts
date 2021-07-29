import {Component, OnInit} from '@angular/core';
import {QaService} from '../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {
  AllPermitDetailsDto,
  ConsolidatedInvoiceDto,
  GenerateInvoiceDto,
  PermitEntityDto, PermitInvoiceDto
} from '../../core/store/data/qa/qa.model';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs';
import swal from 'sweetalert2';

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
  // consolidatedInvoice: GenerateInvoiceDto;
  name: string;
  checkboxGroup: FormGroup;
  submittedValue: any;
  final_array = [];
  selected = [];

    // permitInvoicesIDS = [];


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
    this.qaService.loadInvoiceListWithNoBatchID().subscribe(
        (data: any) => {
          this.allInvoiceData = data;
          // tslint:disable-next-line:max-line-length

          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.commodityDescription, i.brandName, i.totalAmount, i.invoiceNumber, i.permitID]);

          this.dataTable = {
            headerRow: ['Permit Ref N0', 'Commodity Description', 'Brand Name', 'Total Amount', 'Reference Number', 'Select'],
            footerRow: ['Permit Ref N0', 'Commodity Description', 'Brand Name', 'Total Amount', 'Reference Number', 'Select'],
            dataRows: formattedArray

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
      this.selected.splice(this.selected.indexOf(item), 1);
    }
  }

  submit() {
    this.final_array.push(this.selected.sort());
    console.log(this.final_array);
    const selectedRows = this.final_array;
    const permitInvoicesIDS: number[] = [];
    selectedRows.forEach(function (dataValue) {
      for (let i = 0; i <= dataValue.length - 1; i++) {
        const pickedI = dataValue[i];
        const idIndex = dataValue[i].length;
        console.log(`VALUE OF I =${dataValue[i][5]}`);
        const myData = dataValue[i][5];
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
              console.log(data);
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
  }

}
