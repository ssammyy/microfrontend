import {Component, OnInit} from '@angular/core';
import {QaService} from '../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {
    AllPermitDetailsDto,
    ConsolidatedInvoiceDto, GenerateInvoiceDto,
    PermitEntityDto,
    PermitInvoiceDto
} from '../../core/store/data/qa/qa.model';
import swal from "sweetalert2";

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
    consolidatedInvoice: GenerateInvoiceDto;

  constructor(
      private qaService: QaService,
      private router: Router,
  ) { }

  ngOnInit() {
    let formattedArray = [];
    this.qaService.loadInvoiceListWithNoBatchID().subscribe(
        (data: any) => {
          this.allInvoiceData = data;
          // tslint:disable-next-line:max-line-length

          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.commodityDescription, i.brandName, i.totalAmount, i.invoiceNumber, i.permitID]);

          this.dataTable = {
              headerRow: ['Permit Ref N0', 'Commodity Description', 'Brand Name', 'Total Amount', ' Reference Number', ' Select'],
              footerRow: ['Permit Ref N0', 'Commodity Description', 'Brand Name', 'Total Amount', ' Reference Number', ' Select'],
              dataRows: formattedArray

          };

        }
    );

  }

    onClickGenerateInvoice(permitInvoicesID: string[]) {
        this.consolidatedInvoice.permitInvoicesID = permitInvoicesID;

        this.qaService.createInvoiceConsolidatedDetails(this.consolidatedInvoice).subscribe(
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

    // onSelect(rowElement: string) {
    //   this.router.navigate(['/invoiceDetails'], {fragment: rowElement});
    // }

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


}
