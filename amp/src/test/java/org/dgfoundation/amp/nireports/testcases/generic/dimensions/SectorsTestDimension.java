package org.dgfoundation.amp.nireports.testcases.generic.dimensions;

import static org.dgfoundation.amp.nireports.testcases.HNDNode.element;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.nireports.testcases.HNDNode;
import org.dgfoundation.amp.nireports.testcases.HardcodedNiDimension;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;



public class SectorsTestDimension extends HardcodedNiDimension {


    public SectorsTestDimension(String name, int depth) {
        super(name, depth);
    }

    public final static SectorsTestDimension instance = new SectorsTestDimension("sectors", TestModelConstants.SECTORS_DIMENSION_DEPTH);

    @Override
    protected List<HNDNode> buildHardcodedElements() {
        return Arrays.asList(
                element(6236, "110 - EDUCATION", 
                        element(6237, "111 - Education, level unspecified", 
                            element(6238, "11110 - Education policy and administrative management" ), 
                            element(6239, "11120 - Education facilities and training" ), 
                            element(6240, "11130 - Teacher training" ), 
                            element(6241, "11182 - Educational research" ) ) ), 
                    element(6242, "112 - BASIC EDUCATION", 
                        element(6243, "11220 - Primary education" ), 
                        element(6244, "11230 - Basic life skills for youth and adults" ), 
                        element(6245, "11240 - Early childhood education" ) ), 
                    element(6246, "113 - SECONDARY EDUCATION", 
                        element(6247, "11320 - Secondary education" ), 
                        element(6248, "11330 - Vocational training" ) ), 
                    element(6249, "114 - POST-SECONDARY EDUCATION", 
                        element(6250, "11420 - Higher education" ), 
                        element(6251, "11430 - Advanced technical and managerial training" ) ), 
                    element(6252, "120 - HEALTH", 
                        element(6253, "121 - Health, general", 
                            element(6254, "12110 - Health policy and administrative management" ), 
                            element(6255, "12181 - Medical education/training" ), 
                            element(6256, "12182 - Medical research" ), 
                            element(6257, "12191 - Medical services" ) ) ), 
                    element(6258, "122 - BASIC HEALTH", 
                        element(6259, "12220 - Basic health care" ), 
                        element(6260, "12230 - Basic health infrastructure" ), 
                        element(6261, "12240 - Basic nutrition" ), 
                        element(6262, "12250 - Infectious disease control" ), 
                        element(6263, "12261 - Health education" ), 
                        element(6264, "12262 - Malaria control" ), 
                        element(6265, "12263 - Tuberculosis control" ), 
                        element(6266, "12281 - Health personnel development" ) ), 
                    element(6267, "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 
                        element(6268, "13010 - Population policy and administrative management" ), 
                        element(6269, "13020 - Reproductive health care" ), 
                        element(6270, "13030 - Family planning" ), 
                        element(6271, "13040 - STD control including HIV/AIDS" ), 
                        element(6272, "13081 - Personnel development for population and reproductive health" ) ), 
                    element(6273, "140 - WATER AND SANITATION", 
                        element(6274, "14010 - Water sector policy and administrative management" ), 
                        element(6275, "14015 - Water resources conservation (including data collection)" ), 
                        element(6276, "14020 - Water supply and sanitation - large systems" ), 
                        element(6277, "14021 - Water supply - large systems" ), 
                        element(6278, "14022 - Sanitation - large systems" ), 
                        element(6279, "14030 - Basic drinking water supply and basic sanitation" ), 
                        element(6280, "14031 - Basic drinking water supply" ), 
                        element(6281, "14032 - Basic sanitation" ), 
                        element(6282, "14040 - River basins’ development" ), 
                        element(6283, "14050 - Waste management / disposal" ), 
                        element(6284, "14081 - Education and training in water supply and sanitation" ) ), 
                    element(6285, "150 - GOVERNMENT AND CIVIL SOCIETY", 
                        element(6286, "151 - Government and civil society, general", 
                            element(6287, "15110 - Public sector policy and administrative management" ), 
                            element(6288, "15111 - Public finance management" ), 
                            element(6289, "15112 - Decentralisation and support to subnational government" ), 
                            element(6290, "15113 - Anti-corruption organisations and institutions" ), 
                            element(6291, "15130 - Legal and judicial development" ), 
                            element(6292, "15150 - Democratic participation and civil society" ), 
                            element(6293, "15151 - Elections" ), 
                            element(6294, "15152 - Legislatures and political parties" ), 
                            element(6295, "15153 - Media and free flow of information" ), 
                            element(6296, "15160 - Human rights" ), 
                            element(6297, "15170 - Women’s equality organisations and institutions" ) ) ), 
                    element(6298, "152 - CONFLICT PREVENTION AND RESOLUTION, PEACE AND SECURITY", 
                        element(6299, "15210 - Security system management and reform" ), 
                        element(6300, "15220 - Civilian peace-building, conflict prevention and resolution" ), 
                        element(6301, "15230 - Participation in international peacekeeping operations" ), 
                        element(6302, "15240 - Reintegration and SALW control" ), 
                        element(6303, "15250 - Removal of land mines and explosive remnants of war" ), 
                        element(6304, "15261 - Child soldiers (Prevention and demobilisation)" ) ), 
                    element(6305, "160 - OTHER SOCIAL INFRASTRUCTURE AND SERVICES", 
                        element(6306, "16010 - Social/ welfare services" ), 
                        element(6307, "16020 - Employment policy and administrative management" ), 
                        element(6308, "16030 - Housing policy and administrative management" ), 
                        element(6309, "16040 - Low-cost housing" ), 
                        element(6310, "16050 - Multisector aid for basic social services" ), 
                        element(6311, "16061 - Culture and recreation" ), 
                        element(6312, "16062 - Statistical capacity building" ), 
                        element(6313, "16063 - Narcotics control" ), 
                        element(6314, "16064 - Social mitigation of HIV/AIDS" ) ), 
                    element(6315, "210 - TRANSPORT AND STORAGE", 
                        element(6316, "21010 - Transport policy and administrative management" ), 
                        element(6317, "21020 - Road transport" ), 
                        element(6318, "21030 - Rail transport" ), 
                        element(6319, "21040 - Water transport" ), 
                        element(6320, "21050 - Air transport" ), 
                        element(6321, "21061 - Storage" ), 
                        element(6322, "21081 - Education and training in transport and storage" ) ), 
                    element(6323, "220 - COMMUNICATION", 
                        element(6324, "22010 - Communications policy and administrative management" ), 
                        element(6325, "22020 - Telecommunications" ), 
                        element(6326, "22030 - Radio/television/print media" ), 
                        element(6327, "22040 - Information and communication technology (ICT)" ) ), 
                    element(6328, "230 - ENERGY GENERATION AND SUPPLY", 
                        element(6329, "23010 - Energy policy and administrative management" ), 
                        element(6330, "23020 - Power generation/non-renewable sources" ), 
                        element(6331, "23030 - Power generation/renewable sources" ), 
                        element(6332, "23040 - Electrical transmission/ distribution" ), 
                        element(6333, "23050 - Gas distribution" ), 
                        element(6334, "23061 - Oil-fired power plants" ), 
                        element(6335, "23062 - Gas-fired power plants" ), 
                        element(6336, "23063 - Coal-fired power plants" ), 
                        element(6337, "23064 - Nuclear power plants" ), 
                        element(6338, "23065 - Hydro-electric power plants" ), 
                        element(6339, "23066 - Geothermal energy" ), 
                        element(6340, "23067 - Solar energy" ), 
                        element(6341, "23068 - Wind power" ), 
                        element(6342, "23069 - Ocean power" ), 
                        element(6343, "23070 - Biomass" ), 
                        element(6344, "23081 - Energy education/training" ), 
                        element(6345, "23082 - Energy research" ) ), 
                    element(6346, "240 - BANKING AND FINANCIAL SERVICES", 
                        element(6347, "24010 - Financial policy and administrative management" ), 
                        element(6348, "24020 - Monetary institutions" ), 
                        element(6349, "24030 - Formal sector financial intermediaries" ), 
                        element(6350, "24040 - Informal/semi-formal financial intermediaries" ), 
                        element(6351, "24081 - Education/training in banking and financial services" ) ), 
                    element(6352, "250 - BUSINESS AND OTHER SERVICES", 
                        element(6353, "25010 - Business support services and institutions" ), 
                        element(6354, "25020 - Privatisation" ) ), 
                    element(6355, "311 - AGRICULTURE", 
                        element(6356, "31110 - Agricultural policy and administrative management" ), 
                        element(6357, "31120 - Agricultural development" ), 
                        element(6358, "31130 - Agricultural land resources" ), 
                        element(6359, "31140 - Agricultural water resources" ), 
                        element(6360, "31150 - Agricultural inputs" ), 
                        element(6361, "31161 - Food crop production" ), 
                        element(6362, "31162 - Industrial crops/export crops" ), 
                        element(6363, "31163 - Livestock" ), 
                        element(6364, "31164 - Agrarian reform" ), 
                        element(6365, "31165 - Agricultural alternative development" ), 
                        element(6366, "31166 - Agricultural extension" ), 
                        element(6367, "31181 - Agricultural education/training" ), 
                        element(6368, "31182 - Agricultural research" ), 
                        element(6369, "31191 - Agricultural services" ), 
                        element(6370, "31192 - Plant and post-harvest protection and pest control" ), 
                        element(6371, "31193 - Agricultural financial services" ), 
                        element(6372, "31194 - Agricultural co-operatives" ), 
                        element(6373, "31195 - Livestock/veterinary services" ) ), 
                    element(6374, "312 - FORESTRY", 
                        element(6375, "31210 - Forestry policy and administrative management" ), 
                        element(6376, "31220 - Forestry development" ), 
                        element(6377, "31261 - Fuelwood/charcoal" ), 
                        element(6378, "31281 - Forestry education/training" ), 
                        element(6379, "31282 - Forestry research" ), 
                        element(6380, "31291 - Forestry services" ) ), 
                    element(6381, "313 - FISHING", 
                        element(6382, "31310 - Fishing policy and administrative management" ), 
                        element(6383, "31320 - Fishery development" ), 
                        element(6384, "31381 - Fishery education/training" ), 
                        element(6385, "31382 - Fishery research" ), 
                        element(6386, "31391 - Fishery services" ) ), 
                    element(6387, "321 - INDUSTRY", 
                        element(6388, "32110 - Industrial policy and administrative management" ), 
                        element(6389, "32120 - Industrial development" ), 
                        element(6390, "32130 - Small and medium-sized enterprises (SME) development" ), 
                        element(6391, "32140 - Cottage industries and handicraft" ), 
                        element(6392, "32161 - Agro-industries" ), 
                        element(6393, "32162 - Forest industries" ), 
                        element(6394, "32163 - Textiles, leather and substitutes" ), 
                        element(6395, "32164 - Chemicals" ), 
                        element(6396, "32165 - Fertilizer plants" ), 
                        element(6397, "32166 - Cement/lime/plaster" ), 
                        element(6398, "32167 - Energy manufacturing" ), 
                        element(6399, "32168 - Pharmaceutical production" ), 
                        element(6400, "32169 - Basic metal industries" ), 
                        element(6401, "32170 - Non-ferrous metal industries" ), 
                        element(6402, "32171 - Engineering" ), 
                        element(6403, "32172 - Transport equipment industry" ), 
                        element(6404, "32182 - Technological research and development" ) ), 
                    element(6405, "322 - MINERAL RESOURCES AND MINING", 
                        element(6406, "32210 - Mineral/mining policy and administrative management" ), 
                        element(6407, "32220 - Mineral prospection and exploration" ), 
                        element(6408, "32261 - Coal" ), 
                        element(6409, "32262 - Oil and gas" ), 
                        element(6410, "32263 - Ferrous metals" ), 
                        element(6411, "32264 - Nonferrous metals" ), 
                        element(6412, "32265 - Precious metals/materials" ), 
                        element(6413, "32266 - Industrial minerals" ), 
                        element(6414, "32267 - Fertilizer minerals" ), 
                        element(6415, "32268 - Offshore minerals" ) ), 
                    element(6416, "323 - CONSTRUCTION", 
                        element(6417, "32310 - Construction policy and administrative management" ) ), 
                    element(6418, "331 - TRADE POLICY AND REGULATIONS AND TRADE-RELATED ADJUSTMENT", 
                        element(6419, "33110 - Trade policy and administrative management" ), 
                        element(6420, "33120 - Trade facilitation" ), 
                        element(6421, "33130 - Regional trade agreements (RTAs)" ), 
                        element(6422, "33140 - Multilateral trade negotiations" ), 
                        element(6423, "33150 - Trade-related adjustment" ), 
                        element(6424, "33181 - Trade education/training" ) ), 
                    element(6425, "332 - TOURISM", 
                        element(6426, "33210 - Tourism policy and administrative management" ) ), 
                    element(6427, "400 - MULTISECTOR/CROSS-CUTTING", 
                        element(6428, "410 - General environmental protection", 
                            element(6429, "41010 - Environmental policy and administrative management" ), 
                            element(6430, "41020 - Biosphere protection" ), 
                            element(6431, "41030 - Bio-diversity" ), 
                            element(6432, "41040 - Site preservation" ), 
                            element(6433, "41050 - Flood prevention/control" ), 
                            element(6434, "41081 - Environmental education/ training" ), 
                            element(6435, "41082 - Environmental research" ) ) ), 
                    element(6436, "430 - OTHER MULTISECTOR", 
                        element(6437, "43010 - Multisector aid" ), 
                        element(6438, "43030 - Urban development and management" ), 
                        element(6439, "43040 - Rural development" ), 
                        element(6440, "43050 - Non-agricultural alternative development" ), 
                        element(6441, "43081 - Multisector education/training" ), 
                        element(6442, "43082 - Research/scientific institutions" ) ), 
                    element(6443, "500 - COMMODITY AID AND GENERAL PROGRAMME ASSISTANCE", 
                        element(6444, "510 - General budget support", 
                            element(6445, "51010 - General budget support" ) ) ), 
                    element(6446, "520 - DEVELOPMENTAL FOOD AID/FOOD SECURITY ASSISTANCE", 
                        element(6447, "52010 - Food aid/Food security programmes" ) ), 
                    element(6448, "530 - OTHER COMMODITY ASSISTANCE", 
                        element(6449, "53030 - Import support (capital goods)" ), 
                        element(6450, "53040 - Import support (commodities)" ) ), 
                    element(6451, "600 - ACTION RELATING TO DEBT", 
                        element(6452, "60010 - Action relating to debt" ), 
                        element(6453, "60020 - Debt forgiveness" ), 
                        element(6454, "60030 - Relief of multilateral debt" ), 
                        element(6455, "60040 - Rescheduling and refinancing" ), 
                        element(6456, "60061 - Debt for development swap" ), 
                        element(6457, "60062 - Other debt swap" ), 
                        element(6458, "60063 - Debt buy-back" ) ), 
                    element(6459, "700 - HUMANITARIAN AID", 
                        element(6460, "720 - Emergency Response", 
                            element(6461, "72010 - Material relief assistance and services" ), 
                            element(6462, "72040 - Emergency food aid" ), 
                            element(6463, "72050 - Relief co-ordination; protection and support services" ) ) ), 
                    element(6464, "730 - RECONSTRUCTION RELIEF AND REHABILITATION", 
                        element(6465, "73010 - Reconstruction relief and rehabilitation" ) ), 
                    element(6466, "740 - DISASTER PREVENTION AND PREPAREDNESS", 
                        element(6467, "74010 - Disaster prevention and preparedness" ) ), 
                    element(6468, "910 - ADMINISTRATIVE COSTS OF DONORS", 
                        element(6469, "91010 - Administrative costs" ) ), 
                    element(6470, "930 - REFUGEES IN DONOR COUNTRIES", 
                        element(6471, "93010 - Refugees in donor countries" ) ), 
                    element(6472, "998 - UNALLOCATED/ UNSPECIFIED", 
                        element(6473, "99810 - Sectors not specified" ), 
                        element(6474, "99820 - Promotion of development awareness" ) ), 
                    element(6475, "1-DEMOCRATIC COUNTRY", 
                        element(6476, "1.1 Democracy consolidation" ), 
                        element(6477, "1.2 Judiciary system" ), 
                        element(6478, "1.3 Corruption fight" ), 
                        element(6479, "1.4 Borders and law order" ) ), 
                    element(6480, "02 TRANSDNISTRIAN CONFLICT" ), 
                    element(6481, "3 NATIONAL COMPETITIVENESS", 
                        element(6482, "3.1 Business and investment climate" ), 
                        element(6483, "3.2 SME development" ), 
                        element(6484, "3.3 Enterprise efficiency" ), 
                        element(6485, "3.4 Research and innovation" ), 
                        element(6486, "3.5 Infrastructure" ) ), 
                    element(6487, "4 HUMAN RESOURCES", 
                        element(6488, "4.1 Education" ), 
                        element(6489, "4.2 Health" ), 
                        element(6490, "4.3 Labor force" ), 
                        element(6491, "4.4 Social protection" ) ), 
                    element(6492, "5 REGIONAL DEVELOPMENT", 
                        element(6493, "5.1 Regions" ), 
                        element(6494, "5.2 Urban centres" ), 
                        element(6495, "5.3 Social inclusion" ), 
                        element(6496, "5.4 Local Public Authorities" ), 
                        element(6497, "5.5 Environment protection" ) ), 
                    element(6498, "SSN" ));
    }
}
