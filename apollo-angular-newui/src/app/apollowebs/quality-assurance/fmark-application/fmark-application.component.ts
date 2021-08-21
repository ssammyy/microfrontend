import {Component, OnInit} from '@angular/core';
import {AllPermitDetailsDto, FmarkEntityDto, PermitEntityDto} from '../../../core/store/data/qa/qa.model';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import swal from "sweetalert2";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-fmark-application',
    templateUrl: './fmark-application.component.html',
    styleUrls: ['./fmark-application.component.css']
})
export class FmarkApplicationComponent implements OnInit {
    public dataTable: DataTable;
    public allPermitData: PermitEntityDto[];
    public allPermitDetails!: AllPermitDetailsDto;
    fmarkSelected: FmarkEntityDto;
    public permitID!: bigint;

    fmarkForm: FormGroup;

    SMarkTypeID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private qaService: QaService,
        private formBuilder: FormBuilder,
    ) {

    }

  ngOnInit() {
    let formattedArray = [];
    this.dataTable;

      this.qaService.loadPermitAwardedList(String(this.SMarkTypeID)).subscribe(
          (data: any) => {

              this.allPermitData = data;
              // tslint:disable-next-line:max-line-length
              formattedArray = data.map(i => [i.productName, i.tradeMark, i.awardedPermitNumber]);

              this.dataTable = {
                  headerRow: ['Product', 'Brand Name', 'Permit Number'],
                  footerRow: ['Product', 'Brand Name', 'Permit Number'],
                  dataRows: formattedArray

              };

          });

      this.fmarkForm = this.formBuilder.group({
          smarkPermitID: ['', Validators.required]

      });
      //
  }

    get formFmarkForm(): any {
        return this.fmarkForm.controls;
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

    onClickSubmit(valid: boolean) {
        if (valid) {
            this.qaService.generatePermitFMARK(this.fmarkForm.value).subscribe(
                (data) => {
                    this.allPermitDetails = data;
                    this.permitID = this.allPermitDetails.permitDetails.id;
                    console.log(data);
                    swal.fire({
                        title: 'FMARK GENERATED SUCCESSFULLY. PROCEED TO SUBMIT',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    this.router.navigate(['/smarkpermitdetails'], {fragment: String(this.permitID)});
                },
            );
        }
    }
}
