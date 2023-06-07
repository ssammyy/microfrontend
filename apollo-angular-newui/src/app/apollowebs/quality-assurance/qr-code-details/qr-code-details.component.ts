import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {ActivatedRoute, Router} from '@angular/router';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {QRCodeScannedQADto} from '../../../core/store/data/qa/qa.model';

@Component({
  selector: 'app-qr-code-details',
  templateUrl: './qr-code-details.component.html',
  styleUrls: ['./qr-code-details.component.css']
})
export class QrCodeDetailsComponent implements OnInit {
  qrCodeQaScanForm: FormGroup;
  qrCodePermitDetails!: QRCodeScannedQADto;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private qaService: QaService,
      private SpinnerService: NgxSpinnerService,
      private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.route.fragment.subscribe(params => {
      this.qaService.loadQRCodeDetails(params).subscribe(
          (data) => {
            this.qrCodePermitDetails = data;
            this.qrCodeQaScanForm.patchValue(this.qrCodePermitDetails);
          },
      );
    });

    this.qrCodeQaScanForm = this.formBuilder.group({
      productName: [{value: '', disabled: true}, Validators.required],
      tradeMark: [{value: '', disabled: true}, Validators.required],
      awardedPermitNumber: [{value: '', disabled: true}, Validators.required],
      dateOfIssue: [{value: '', disabled: true}, Validators.required],
      dateOfExpiry: [{value: '', disabled: true}, Validators.required]
    });
  }

}
