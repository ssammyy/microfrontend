import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MsService} from "../../../../../core/store/data/ms/ms.service";
import {County, CountyService, loadCountyId, selectCountyIdData, Town, TownService} from "../../../../../core/store";
import {Store} from "@ngrx/store";
import {Observable, throwError} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {BatchFileFuelSaveDto} from "../../../../../core/store/data/ms/ms.model";

@Component({
  selector: 'app-epra-batch-new',
  templateUrl: './epra-batch-new.component.html',
  styleUrls: ['./epra-batch-new.component.css']
})
export class EpraBatchNewComponent implements OnInit {

  addNewBatchForm!: FormGroup;
  dataSave: BatchFileFuelSaveDto;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  constructor(private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialogRef: MatDialogRef<any>,
              private countyService: CountyService,
              private townService: TownService,
              private msService: MsService,
              private SpinnerService: NgxSpinnerService,
              private store$: Store<any>
  ) {
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$;
    countyService.getAll().subscribe();
  }

  ngOnInit(): void {
    this.addNewBatchForm = this.formBuilder.group({
      county: ['', Validators.required],
      town: ['', Validators.required],
      remarks: [''],
    });
  }

  get formNewBatchForm(): any {
    return this.addNewBatchForm.controls;
  }


  updateSelectedCounty() {
    this.selectedCounty = this.addNewBatchForm?.get('county')?.value;
    console.log(`county set to ${this.selectedCounty}`);
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
        (d) => {
          if (d) {
            console.log(`Select county inside is ${d}`);
            return this.townService.getAll();
          } else {
            return throwError('Invalid request, County id is required');
          }
        }
    );

  }

  updateSelectedTown() {
    this.selectedTown = this.addNewBatchForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }

  onClickSaveNewBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSave = {...this.dataSave, ...this.addNewBatchForm.value};
      this.msService.addNewMSFuelBatch(this.dataSave).subscribe(
          (data: any) => {
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess("NEW FUEL BATCH CREATED SUCCESSFUL",()=>{
              this.dialogRef.close(true)
            })
          },
      );
    }
  }
}
