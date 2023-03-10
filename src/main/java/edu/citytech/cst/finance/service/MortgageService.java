package edu.citytech.cst.finance.service;

import edu.citytech.cst.finance.model.MortgageSummary;
import edu.citytech.cst.finance.model.Payment;

import java.util.Date;

public class MortgageService {
    public static double getSchedulePayments(double loanAmount, int years, double interest) {

        double monthlyInterest = getMonthlyInterestRate(loanAmount,interest);
        double monthlyPayment = loanAmount * monthlyInterest /
                (1-1/ Math.pow(1+ monthlyInterest,years *12));
        return monthlyPayment;
    }

    public static double getMonthlyInterestRate(double loanAmount, double interest){
        double monthlyInterest= interest /12;
        return monthlyInterest;
    }

    public static double getMonthlyInterestPayment(double loanAmount, double interest){
        return getMonthlyInterestRate(loanAmount,interest)* loanAmount;
    }

    public static MortgageSummary getSummary(double loanAmount, int years
            ,double interest){

        Payment[] payments = getAllMonthlyPayments( loanAmount, years
                                                  ,interest);

        Payment lastPayment = payments[payments.length-1];
       double cumulativeInterest = lastPayment.getCumulativeInterest();


       double totalPrincipal = 0;
       for (Payment p:payments) {
            totalPrincipal += p.getPrincipal();

        }


        MortgageSummary summary = new MortgageSummary(cumulativeInterest,totalPrincipal);


    return summary;
    }


    public static Payment[] getAllMonthlyPayments(double loanAmount, int years
                                                  ,double interest){

        int numberOfMonths = years *12;

        double interestPayment = getMonthlyInterestPayment(loanAmount,interest);
        Payment[] paymentDetails = new Payment[numberOfMonths];
        double cumulativeInterest = interestPayment;
        double schedulePayment = getSchedulePayments(loanAmount,years,interest);
        for (int i = 0; i < numberOfMonths ; i++) {

            Payment payment=  MortgageService.
                    getDetailPayment(i + 1,new Date(),loanAmount,
                            schedulePayment, years,interest,
                    interestPayment,cumulativeInterest);

            paymentDetails[i] = payment;
            loanAmount = payment.getEndingBalance();
            interestPayment = getMonthlyInterestPayment(loanAmount,interest);
            cumulativeInterest += interestPayment;


        }





        return paymentDetails;
    }

    public static Payment getDetailPayment(int paymentNumber, Date paymentDate, final double loanAmount, final double scheduledPayment,final int years, final double interest,
                                           final double interestPayment,final double cumulativeInterest){



        double beginningBalance=0,
         extraPayment=0,  totalPayment=0,  principal=0, endingBalance=0;


         beginningBalance = loanAmount ;

        totalPayment = extraPayment + scheduledPayment;

        principal = scheduledPayment - interestPayment;
        endingBalance = loanAmount - principal;


        Payment payment=new Payment(paymentNumber, paymentDate,
                beginningBalance, scheduledPayment,
                extraPayment, totalPayment,
                principal, interest,
                endingBalance, cumulativeInterest);



        return payment;
    }

}
