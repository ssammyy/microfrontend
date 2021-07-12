import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {QaService} from '../../core/store/data/qa/qa.service';
import {AllPermitDetailsDto} from '../../core/store/data/qa/qa.model';
import swal from "sweetalert2";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-dmark',
    templateUrl: './dmark.component.html',
    styleUrls: ['./dmark.component.css']
})
export class DmarkComponent implements OnInit, AfterViewInit {

    public dataTable: DataTable;
    public permitID!: string;
    public allPermitDetails!: AllPermitDetailsDto;


    constructor(
        private route: ActivatedRoute,
        private qaService: QaService
    ) {
    }

    ngOnInit(): void {
        this.getSelectedPermit();

        this.dataTable = {
            headerRow: ['documents attached details', 'file name', 'file type', 'document description', 'version number', 'view'],
            footerRow: ['documents attached details', 'file name', 'file type', 'document description', 'version number', 'view'],

            dataRows: [
                ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']
            ]
        };
    }


    public getSelectedPermit(): void {
        this.route.fragment.subscribe(params => {
            this.permitID = params;
            console.log(this.permitID);
            this.qaService.loadPermitDetails(this.permitID).subscribe(
                (data: AllPermitDetailsDto) => {
                    this.allPermitDetails = data;
                    // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);

                },
            );
        });

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

    submitApplication(): void {
        this.qaService.submitPermitForReview(this.permitID).subscribe(
            (data: AllPermitDetailsDto) => {
                this.allPermitDetails = data;
                swal.fire({
                    title: 'DMARK SUBMITTED SUCCESSFULLY PENDING REVIEW FROM PCM!',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success form-wizard-next-btn ',
                    },
                    icon: 'success'
                });
                // this.onUpdateReturnToList();
            },
        );
    }
}
