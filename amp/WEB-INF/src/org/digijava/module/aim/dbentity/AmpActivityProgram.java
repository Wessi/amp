package org.digijava.module.aim.dbentity;

import java.util.ArrayList;

import org.digijava.module.aim.util.Output;
import org.digijava.module.aim.util.ProgramUtil;

public class AmpActivityProgram implements Versionable {

        private Long ampActivityProgramId;
        private Long programPercentage;
        private AmpTheme program;
        private AmpActivity activity;
        private AmpActivityProgramSettings programSetting;
        public Long getAmpActivityProgramId() {
                return ampActivityProgramId;
        }

        public Long getProgramPercentage() {
                return programPercentage;
        }

        public AmpTheme getProgram() {
                return program;
        }

        public AmpActivity getActivity() {
                return activity;
        }

        public AmpActivityProgramSettings getProgramSetting() {
                return programSetting;
        }

        public void setAmpActivityProgramId(Long ampActivityProgramId) {
                this.ampActivityProgramId = ampActivityProgramId;
        }

        public void setProgramPercentage(Long programPercentage) {
                this.programPercentage = programPercentage;
        }

        public void setProgram(AmpTheme program) {
                this.program = program;
        }

        public void setActivity(AmpActivity activity) {
                this.activity = activity;
        }

        public void setProgramSetting(AmpActivityProgramSettings programSetting) {
                this.programSetting = programSetting;
        }

        public String getHierarchyNames() {
                String names = "";
                names = ProgramUtil.printHierarchyNames(this.program);
                names += "[" + this.program.getName() + "]";
                return names;
        }
        
    @Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivityProgram aux = (AmpActivityProgram) obj;
		if (this.program.getAmpThemeId().equals(aux.program.getAmpThemeId())) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] { "Name:&nbsp;" }, new Object[] { this.program.getName() }));
		out.getOutputs()
				.add(new Output(null, new String[] { "Percentage:&nbsp;" }, new Object[] { this.programPercentage }));
		return out;
	}

	@Override
	public Object getValue() {
		String ret = "";
		ret = "" + this.programPercentage;
		return ret;
	}
}
