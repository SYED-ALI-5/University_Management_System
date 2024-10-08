package Classes;

public class Fee
{
        protected double totalFee;
        protected double FeePaid;

        public Fee(double TotalFee, double PaidFee) {
            setTotalFee(TotalFee);
            setFeePaid(PaidFee);
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }

        public void setFeePaid(double feePaid) {
            FeePaid = feePaid;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public double getFeePaid() {
            return FeePaid;
        }

        private double FeeReamaining = getTotalFee() - getFeePaid();

        public double getFeeReamaining() {
            return FeeReamaining;
        }
    }

